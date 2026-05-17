package cn.edu.nju.cs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter extends MiniJavaParserBaseVisitor<Value> {
    private final Env env = new Env();
    private final Map<String, List<MethodDef>> methods = new HashMap<>();
    private final Map<String, ClassDef> classes = new HashMap<>();
    private final List<String> duplicateClassNames = new ArrayList<>();
    private int loopDepth = 0;
    private Value.TypeInfo currentReturnType = Value.VOID_TYPE;
    private Value currentThis = null;
    private String currentClass = null;

    private record MethodDef(
            String owner,
            String name,
            Value.TypeInfo returnType,
            List<Value.TypeInfo> parameterTypes,
            List<String> parameterNames,
            MiniJavaParser.BlockContext body
    ) {}

    private record FieldDef(
            String owner,
            String name,
            Value.TypeInfo type,
            MiniJavaParser.VariableInitializerContext initializer
    ) {
        String key() {
            return owner + "#" + name;
        }
    }

    private record ConstructorDef(
            String owner,
            List<Value.TypeInfo> parameterTypes,
            List<String> parameterNames,
            MiniJavaParser.BlockContext body
    ) {}

    private static final class ClassDef {
        final String name;
        final String parent;
        final List<FieldDef> fields = new ArrayList<>();
        final Map<String, List<MethodDef>> methods = new HashMap<>();
        final List<ConstructorDef> constructors = new ArrayList<>();

        ClassDef(String name, String parent) {
            this.name = name;
            this.parent = parent;
        }
    }

    private static class BreakSignal extends RuntimeException {}
    private static class ContinueSignal extends RuntimeException {}
    private static class ReturnSignal extends RuntimeException {
        final Value value;
        ReturnSignal(Value value) {
            this.value = value;
        }
    }

    @Override
    public Value visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
        env.setTypeAssignable(this::isAssignableType);
        env.setPrinter(this::printable);
        for (MiniJavaParser.ClassDeclarationContext classCtx : ctx.classDeclaration()) {
            collectClass(classCtx);
        }
        for (MiniJavaParser.MethodDeclarationContext method : ctx.methodDeclaration()) {
            MethodDef def = buildMethod(null, method);
            List<MethodDef> overloads = methods.computeIfAbsent(def.name, ignored -> new ArrayList<>());
            overloads.add(def);
        }
        Value result = callMethod("main", List.of(), true);
        System.out.println("Process exits with " + result.asInt() + ".");
        System.exit(result.asInt());
        return Value.VOID;
    }

    private boolean isAllowedMainEntryConflict(MethodDef existing, MethodDef def) {
        if (!existing.name.equals("main") || !def.name.equals("main")) return false;
        if (!existing.parameterTypes.isEmpty() || !def.parameterTypes.isEmpty()) return false;
        if (existing.returnType.equals(Value.VOID_TYPE) || def.returnType.equals(Value.VOID_TYPE)) return false;
        return existing.returnType.equals(Value.INT) != def.returnType.equals(Value.INT);
    }

    private void collectClass(MiniJavaParser.ClassDeclarationContext ctx) {
        String name = ctx.identifier().getText();
        String parent = ctx.parentClassDeclaration() == null ? null : ctx.parentClassDeclaration().identifier().getText();
        if (classes.containsKey(name)) {
            duplicateClassNames.add(name);
            return;
        }
        ClassDef def = new ClassDef(name, parent);
        classes.put(name, def);

        for (MiniJavaParser.ClassBodyDeclarationContext member : ctx.classBody().classBodyDeclaration()) {
            if (member.fieldDeclaration() != null) {
                MiniJavaParser.FieldDeclarationContext field = member.fieldDeclaration();
                MiniJavaParser.VariableDeclaratorContext declarator = field.variableDeclarator();
                FieldDef fieldDef = new FieldDef(
                        name,
                        declarator.identifier().getText(),
                        parseType(field.typeType()),
                        declarator.variableInitializer()
                );
                def.fields.add(fieldDef);
            } else if (member.methodDeclaration() != null) {
                MethodDef method = buildMethod(name, member.methodDeclaration());
                List<MethodDef> overloads = def.methods.computeIfAbsent(method.name, ignored -> new ArrayList<>());
                overloads.add(method);
            } else if (member.constructorDeclaration() != null) {
                ConstructorDef constructor = buildConstructor(name, member.constructorDeclaration());
                def.constructors.add(constructor);
            }
        }
    }

    private FieldDef findDeclaredField(ClassDef def, String name) {
        FieldDef result = null;
        for (FieldDef field : def.fields) {
            if (field.name.equals(name)) {
                if (result != null) Value.fail(34);
                result = field;
            }
        }
        return result;
    }

    private void validateClasses() {
        for (ClassDef def : classes.values()) {
            if (def.parent != null && !classes.containsKey(def.parent)) Value.fail(34);
            detectCycle(def.name, new ArrayList<>());
            for (FieldDef field : def.fields) validateType(field.type);
            for (List<MethodDef> overloads : def.methods.values()) {
                for (MethodDef method : overloads) {
                    validateType(method.returnType);
                    for (Value.TypeInfo type : method.parameterTypes) validateType(type);
                }
            }
            for (ConstructorDef constructor : def.constructors) {
                for (Value.TypeInfo type : constructor.parameterTypes) validateType(type);
            }
        }
    }

    private MethodDef findInheritedMethod(String parent, String name, List<Value.TypeInfo> parameterTypes) {
        if (parent != null) validateClassUsable(parent);
        for (String cls = parent; cls != null; cls = classes.get(cls).parent) {
            ClassDef def = classes.get(cls);
            if (def == null) Value.fail(34);
            for (MethodDef method : def.methods.getOrDefault(name, List.of())) {
                if (method.parameterTypes.equals(parameterTypes)) return method;
            }
        }
        return null;
    }

    private void validateType(Value.TypeInfo type) {
        if (type.base == Value.Base.CLASS && !classes.containsKey(type.className)) Value.fail(34);
    }

    private boolean isKnownType(Value.TypeInfo type) {
        return type.base != Value.Base.CLASS || classes.containsKey(type.className);
    }

    private void validateClassUsable(String name) {
        if (duplicateClassNames.contains(name)) Value.fail(34);
        detectCycle(name, new ArrayList<>());
    }

    private void detectCycle(String name, List<String> path) {
        if (duplicateClassNames.contains(name)) Value.fail(34);
        if (path.contains(name)) Value.fail(34);
        ClassDef def = classes.get(name);
        if (def == null) Value.fail(34);
        if (def.parent == null) return;
        path.add(name);
        detectCycle(def.parent, path);
        path.remove(path.size() - 1);
    }

    @Override
    public Value visitBlock(MiniJavaParser.BlockContext ctx) {
        env.enterScope();
        try {
            visitBlockStatements(ctx, 0);
            return Value.VOID;
        } finally {
            env.exitScope();
        }
    }

    private void visitBlockStatements(MiniJavaParser.BlockContext ctx, int start) {
        for (int i = start; i < ctx.blockStatement().size(); i++) {
            visit(ctx.blockStatement(i));
        }
    }

    @Override
    public Value visitBlockStatement(MiniJavaParser.BlockStatementContext ctx) {
        if (ctx.localVariableDeclaration() != null) {
            return visit(ctx.localVariableDeclaration());
        }
        return visit(ctx.statement());
    }

    @Override
    public Value visitLocalVariableDeclaration(MiniJavaParser.LocalVariableDeclarationContext ctx) {
        if (ctx.VAR() != null) {
            Value value = visit(ctx.expression());
            if (value.type.equals(Value.VOID_TYPE)) Value.fail(34);
            if (value.type.equals(Value.NULL_TYPE)) Value.fail(34);
            env.declare(ctx.identifier().getText(), value.type, value);
            return Value.VOID;
        }

        Value.TypeInfo type = parseType(ctx.typeType());
        validateType(type);
        MiniJavaParser.VariableDeclaratorContext declarator = ctx.variableDeclarator();
        Value value;
        if (declarator.variableInitializer() == null) {
            value = env.defaultValue(type);
        } else {
            value = evalInitializer(declarator.variableInitializer(), type);
        }
        env.declare(declarator.identifier().getText(), type, value);
        return Value.VOID;
    }

    @Override
    public Value visitPrimary(MiniJavaParser.PrimaryContext ctx) {
        if (ctx.literal() != null) return visit(ctx.literal());
        if (ctx.identifier() != null) {
            String name = ctx.identifier().getText();
            Value local = env.loadOrNull(name);
            if (local != null) return local;
            if (currentThis != null) return fieldLValue(currentThis, currentClass, name).get();
            Value.fail(34);
        }
        if (ctx.THIS() != null) {
            Value.fail(34);
        }
        if (ctx.SUPER() != null) {
            Value.fail(34);
        }
        return visit(ctx.expression());
    }

    @Override
    public Value visitLiteral(MiniJavaParser.LiteralContext ctx) {
        String text = ctx.getText();
        if (ctx.BOOL_LITERAL() != null) return Value.boolValue(Boolean.parseBoolean(text));
        if (ctx.STRING_LITERAL() != null) return Value.stringValue(text.substring(1, text.length() - 1));
        if (ctx.DECIMAL_LITERAL() != null) {
            return Value.decimalInt(new BigInteger(text.replace("_", "")).intValue());
        }
        if (ctx.NULL_LITERAL() != null) return Value.NULL;
        if (ctx.CHAR_LITERAL() != null) return Value.charValue(parseChar(text));
        Value.fail(34);
        return null;
    }

    @Override
    public Value visitExpression(MiniJavaParser.ExpressionContext ctx) {
        if (ctx.primary() != null) return visit(ctx.primary());
        if (ctx.methodCall() != null && ctx.expression().isEmpty()) return visitMethodCall(ctx.methodCall());
        if (ctx.NEW() != null) return visitCreator(ctx.creator());

        if (ctx.postfix != null) {
            Env.LValue target = lvalue(ctx.expression(0));
            Value oldValue = target.get();
            requireIntegral(oldValue);
            Value updated = incremented(oldValue, ctx.postfix.getText().equals("++") ? 1 : -1);
            target.set(updated);
            return oldValue;
        }

        if (ctx.prefix != null) {
            String op = ctx.prefix.getText();
            if (op.equals("++") || op.equals("--")) {
                Env.LValue target = lvalue(ctx.expression(0));
                Value oldValue = target.get();
                requireIntegral(oldValue);
                return target.set(incremented(oldValue, op.equals("++") ? 1 : -1));
            }
            Value value = visit(ctx.expression(0));
            return switch (op) {
                case "+" -> {
                    requireIntegral(value);
                    yield value.decimalLiteral ? Value.decimalInt(value.asInt()) : Value.intValue(value.asInt());
                }
                case "-" -> {
                    requireIntegral(value);
                    yield value.decimalLiteral ? Value.decimalInt(-value.asInt()) : Value.intValue(-value.asInt());
                }
                case "~" -> {
                    requireIntegral(value);
                    yield Value.intValue(~value.asInt());
                }
                case "not" -> Value.boolValue(!value.asBool());
                default -> {
                    Value.fail(34);
                    yield null;
                }
            };
        }

        if (ctx.INSTANCEOF() != null) {
            Value.TypeInfo target = parseType(ctx.typeType());
            validateType(target);
            return evalInstanceOf(visit(ctx.expression(0)), target);
        }

        if (ctx.typeType() != null) {
            Value.TypeInfo type = parseType(ctx.typeType());
            validateType(type);
            Value value = visit(ctx.expression(0));
            if (type.isArray()) return env.castValue(value, type);
            if (type.isClass()) return castObject(value, type);
            if (type.equals(Value.BOOL) || type.equals(Value.STRING)) Value.fail(34);
            requireIntegral(value);
            return type.equals(Value.INT) ? Value.intValue(value.asInt()) : Value.charValue(value.asInt());
        }

        if (ctx.LBRACK() != null && ctx.bop == null) {
            return arrayElement(ctx.expression(0), ctx.expression(1)).get();
        }

        if (ctx.bop == null) Value.fail(34);
        String op = ctx.bop.getText();
        if (op.equals(".")) {
            return evalDot(ctx);
        }
        if (isAssignment(op)) {
            Env.LValue target = lvalue(ctx.expression(0));
            if (!op.equals("=")) target.get();
            Value right = visit(ctx.expression(1));
            return env.applyAssignment(target, op, right);
        }

        if (op.equals("?")) {
            Value condition = visit(ctx.expression(0));
            return condition.asBool() ? visit(ctx.expression(1)) : visit(ctx.expression(2));
        }
        Value left = visit(ctx.expression(0));
        if (op.equals("and")) {
            if (!left.asBool()) return Value.boolValue(false);
            return Value.boolValue(visit(ctx.expression(1)).asBool());
        }
        if (op.equals("or")) {
            if (left.asBool()) return Value.boolValue(true);
            return Value.boolValue(visit(ctx.expression(1)).asBool());
        }

        Value right = visit(ctx.expression(1));
        return evalBinary(left, op, right);
    }

    @Override
    public Value visitStatement(MiniJavaParser.StatementContext ctx) {
        if (ctx.block() != null) return visit(ctx.block());
        if (ctx.IF() != null) {
            Value cond = visit(ctx.parExpression());
            if (!cond.type.equals(Value.BOOL)) Value.fail(34);
            if (cond.asBool()) return visit(ctx.statement(0));
            if (ctx.ELSE() != null) return visit(ctx.statement(1));
            return Value.VOID;
        }
        if (ctx.FOR() != null) return visitFor(ctx);
        if (ctx.WHILE() != null) return visitWhile(ctx);
        if (ctx.RETURN() != null) {
            Value value = ctx.expression() == null ? Value.VOID : visit(ctx.expression());
            if (currentReturnType.equals(Value.VOID_TYPE)) {
                if (!value.type.equals(Value.VOID_TYPE)) Value.fail(34);
                throw new ReturnSignal(Value.VOID);
            }
            if (value.type.equals(Value.VOID_TYPE)) Value.fail(34);
            throw new ReturnSignal(env.castReturnValue(value, currentReturnType));
        }
        if (ctx.BREAK() != null) {
            if (loopDepth == 0) Value.fail(34);
            throw new BreakSignal();
        }
        if (ctx.CONTINUE() != null) {
            if (loopDepth == 0) Value.fail(34);
            throw new ContinueSignal();
        }
        if (ctx.expression() != null) return visit(ctx.expression());
        if (ctx.SEMI() != null) return Value.VOID;
        Value.fail(34);
        return null;
    }

    @Override
    public Value visitForInit(MiniJavaParser.ForInitContext ctx) {
        if (ctx.localVariableDeclaration() != null) return visit(ctx.localVariableDeclaration());
        for (MiniJavaParser.ExpressionContext expr : ctx.expressionList().expression()) visit(expr);
        return Value.VOID;
    }

    @Override
    public Value visitParExpression(MiniJavaParser.ParExpressionContext ctx) {
        return visit(ctx.expression());
    }

    private Value visitFor(MiniJavaParser.StatementContext ctx) {
        MiniJavaParser.ForControlContext control = ctx.forControl();
        env.enterScope();
        loopDepth++;
        try {
            if (control.forInit() != null) visit(control.forInit());
            while (true) {
                if (control.expression() != null && !visit(control.expression()).asBool()) break;
                try {
                    visit(ctx.statement(0));
                } catch (BreakSignal e) {
                    break;
                } catch (ContinueSignal ignored) {
                    // Continue still evaluates the update expression.
                }
                if (control.forUpdate != null) {
                    for (MiniJavaParser.ExpressionContext expr : control.forUpdate.expression()) visit(expr);
                }
            }
            return Value.VOID;
        } finally {
            loopDepth--;
            env.exitScope();
        }
    }

    private Value visitWhile(MiniJavaParser.StatementContext ctx) {
        loopDepth++;
        try {
            while (visit(ctx.parExpression()).asBool()) {
                try {
                    visit(ctx.statement(0));
                } catch (BreakSignal e) {
                    break;
                } catch (ContinueSignal ignored) {
                    // Continue jumps to the next condition check.
                }
            }
            return Value.VOID;
        } finally {
            loopDepth--;
        }
    }

    private MethodDef buildMethod(String owner, MiniJavaParser.MethodDeclarationContext ctx) {
        Value.TypeInfo returnType = ctx.VOID() != null ? Value.VOID_TYPE : parseType(ctx.typeType());
        List<Value.TypeInfo> parameterTypes = new ArrayList<>();
        List<String> parameterNames = new ArrayList<>();
        if (ctx.formalParameters().formalParameterList() != null) {
            for (MiniJavaParser.FormalParameterContext param : ctx.formalParameters().formalParameterList().formalParameter()) {
                parameterTypes.add(parseType(param.typeType()));
                parameterNames.add(param.identifier().getText());
            }
        }
        return new MethodDef(owner, ctx.identifier().getText(), returnType, parameterTypes, parameterNames, ctx.methodBody);
    }

    private ConstructorDef buildConstructor(String className, MiniJavaParser.ConstructorDeclarationContext ctx) {
        List<Value.TypeInfo> parameterTypes = new ArrayList<>();
        List<String> parameterNames = new ArrayList<>();
        if (ctx.formalParameters().formalParameterList() != null) {
            for (MiniJavaParser.FormalParameterContext param : ctx.formalParameters().formalParameterList().formalParameter()) {
                parameterTypes.add(parseType(param.typeType()));
                parameterNames.add(param.identifier().getText());
            }
        }
        return new ConstructorDef(ctx.identifier().getText(), parameterTypes, parameterNames, ctx.constructorBody);
    }

    private Value callMethod(String name, List<Value> args, boolean entryCall) {
        if (!entryCall) {
            MethodDef userMethod = resolveMethodOrNull(name, args, false);
            if (userMethod != null) return invokeMethod(userMethod, args);
            Value builtIn = tryBuiltIn(name, args);
            if (builtIn != null) return builtIn;
        }
        MethodDef def = resolveMethod(name, args, entryCall);
        return invokeMethod(def, args);
    }

    private Value invokeMethod(MethodDef def, List<Value> args) {
        return invokeMethod(def, args, null, null);
    }

    private Value invokeMethod(MethodDef def, List<Value> args, Value receiver, String dispatchClass) {
        validateInvokedMethod(def);
        env.pushFrame();
        env.enterScope();
        Value.TypeInfo previousReturnType = currentReturnType;
        int previousLoopDepth = loopDepth;
        Value previousThis = currentThis;
        String previousClass = currentClass;
        currentReturnType = def.returnType;
        loopDepth = 0;
        currentThis = receiver;
        currentClass = dispatchClass;
        try {
            for (int i = 0; i < args.size(); i++) {
                env.declare(def.parameterNames.get(i), def.parameterTypes.get(i), convertForCall(args.get(i), def.parameterTypes.get(i)));
            }
            try {
                visitBlockStatements(def.body, 0);
            } catch (ReturnSignal signal) {
                return def.returnType.equals(Value.CHAR) ? signal.value.markCharMethodReturn() : signal.value;
            }
            if (!def.returnType.equals(Value.VOID_TYPE)) Value.fail(34);
            return Value.VOID;
        } finally {
            currentReturnType = previousReturnType;
            loopDepth = previousLoopDepth;
            currentThis = previousThis;
            currentClass = previousClass;
            env.exitScope();
            env.popFrame();
        }
    }

    private void validateInvokedMethod(MethodDef def) {
        validateMethodSignature(def);
        if (def.owner == null) return;
        ClassDef owner = classes.get(def.owner);
        if (owner == null) Value.fail(34);
        validateClassUsable(owner.name);
        MethodDef inherited = findInheritedMethod(owner.parent, def.name, def.parameterTypes);
        if (inherited != null && !inherited.returnType.equals(def.returnType)) Value.fail(34);
    }

    private void validateMethodSignature(MethodDef def) {
        validateType(def.returnType);
        for (Value.TypeInfo type : def.parameterTypes) validateType(type);
    }

    private void validateConstructorSignature(ConstructorDef def, String className) {
        if (!def.owner.equals(className)) Value.fail(34);
        for (Value.TypeInfo type : def.parameterTypes) validateType(type);
    }

    private MethodDef resolveMethod(String name, List<Value> args, boolean entryCall) {
        MethodDef selected = resolveMethodOrNull(name, args, entryCall);
        if (selected == null) Value.fail(34);
        if (entryCall && !selected.returnType.equals(Value.INT)) Value.fail(34);
        return selected;
    }

    private MethodDef resolveMethodOrNull(String name, List<Value> args, boolean entryCall) {
        List<MethodDef> candidates = methods.getOrDefault(name, List.of());
        if (entryCall) {
            MethodDef entry = null;
            for (MethodDef def : candidates) {
                if (!def.name.equals("main") || !def.parameterTypes.isEmpty()) continue;
                if (entry != null) Value.fail(34);
                entry = def;
            }
            return entry;
        }

        List<ResolvedMethod> matches = new ArrayList<>();
        for (MethodDef def : candidates) {
            if (def.parameterTypes.size() != args.size()) continue;
            int score = 0;
            boolean ok = true;
            for (int i = 0; i < args.size(); i++) {
                if (!isKnownType(def.parameterTypes.get(i))) {
                    ok = false;
                    break;
                }
                int cost = conversionCost(args.get(i), def.parameterTypes.get(i));
                if (cost < 0) {
                    ok = false;
                    break;
                }
                score += cost;
            }
            if (ok) matches.add(new ResolvedMethod(def, score));
        }
        if (matches.isEmpty()) return null;
        matches.sort(Comparator.comparingInt(ResolvedMethod::score));
        if (matches.size() > 1 && matches.get(0).score == matches.get(1).score) Value.fail(34);
        return matches.get(0).def;
    }

    private record ResolvedMethod(MethodDef def, int score) {}

    private int conversionCost(Value value, Value.TypeInfo target) {
        if (value.type.equals(target)) return 0;
        if (value.type.equals(Value.CHAR) && target.equals(Value.INT)) return 1;
        if (value.type.equals(Value.NULL_TYPE) && target.isReference()) return 1;
        if (value.type.isClass() && target.isClass() && !value.isNull() && isAssignableType(value.type, target)) {
            return 1;
        }
        if (value.type.isClass() && value.isNull() && target.isClass() && isAssignableType(value.type, target)) return 1;
        return -1;
    }

    private Value convertForCall(Value value, Value.TypeInfo target) {
        if (value.type.equals(target)) return value;
        if (value.type.equals(Value.CHAR) && target.equals(Value.INT)) return Value.intValue(value.asInt());
        if (value.type.equals(Value.NULL_TYPE) && target.isReference()) return Value.nullValue(target);
        if (target.isClass() && value.type.isClass() && !value.isNull() && isAssignableType(value.type, target)) {
            return Value.objectValue(target, value.object());
        }
        if (target.isClass() && value.type.isClass() && value.isNull() && isAssignableType(value.type, target)) {
            return Value.nullValue(target);
        }
        Value.fail(34);
        return null;
    }

    @Override
    public Value visitMethodCall(MiniJavaParser.MethodCallContext ctx) {
        List<Value> args = methodArgs(ctx.arguments());
        if (ctx.THIS() != null) {
            if (currentThis == null) Value.fail(34);
            Value.fail(34);
        }
        if (ctx.SUPER() != null) {
            Value.fail(34);
        }
        String name = ctx.identifier().getText();
        if (currentThis != null) {
            MethodDef method = resolveInstanceMethodOrNull(currentClass, name, args);
            if (method != null) return invokeSelectedInstanceMethod(currentThis, method, args, true);
        }
        return callMethod(name, args, false);
    }

    private Value tryBuiltIn(String name, List<Value> args) {
        return switch (name) {
            case "print" -> {
                if (args.size() != 1) Value.fail(34);
                if (args.get(0).type.equals(Value.VOID_TYPE)) Value.fail(34);
                System.out.print(printable(args.get(0)));
                yield Value.VOID;
            }
            case "println" -> {
                if (args.isEmpty()) {
                    System.out.println();
                } else if (args.size() == 1) {
                    if (args.get(0).type.equals(Value.VOID_TYPE)) Value.fail(34);
                    System.out.println(printable(args.get(0)));
                } else {
                    Value.fail(34);
                }
                yield Value.VOID;
            }
            case "assert" -> {
                if (args.size() != 1 || !args.get(0).type.equals(Value.BOOL)) Value.fail(34);
                if (!args.get(0).asBool()) Value.fail(33);
                yield Value.VOID;
            }
            case "length" -> {
                if (args.size() != 1) Value.fail(34);
                Value arg = args.get(0);
                if (arg.isNull()) Value.fail(34);
                if (arg.type.equals(Value.STRING)) yield Value.intValue(arg.asString().length());
                if (arg.isArray()) yield Value.intValue(arg.array().elements.size());
                Value.fail(34);
                yield null;
            }
            case "to_char_array" -> {
                if (args.size() != 1 || !args.get(0).type.equals(Value.STRING)) Value.fail(34);
                List<Value> chars = new ArrayList<>();
                for (char c : args.get(0).asString().toCharArray()) chars.add(Value.charValue(c));
                yield Value.arrayValue(Value.TypeInfo.array(Value.Base.CHAR, 1), chars);
            }
            case "to_string" -> {
                if (args.size() != 1) Value.fail(34);
                Value arg = args.get(0);
                if (arg.isNull()) Value.fail(34);
                Value.TypeInfo charArray = Value.TypeInfo.array(Value.Base.CHAR, 1);
                if (!arg.type.equals(charArray)) Value.fail(34);
                StringBuilder sb = new StringBuilder();
                for (Value ch : arg.array().elements) sb.append(ch.asString());
                yield Value.stringValue(sb.toString());
            }
            case "atoi" -> {
                if (args.size() != 1 || !args.get(0).type.equals(Value.STRING)) Value.fail(34);
                try {
                    yield Value.intValue(Integer.parseInt(args.get(0).asString()));
                } catch (NumberFormatException e) {
                    Value.fail(34);
                    yield null;
                }
            }
            case "itoa" -> {
                if (args.size() != 1 || !args.get(0).type.isIntegral()) Value.fail(34);
                yield Value.stringValue(String.valueOf(args.get(0).asInt()));
            }
            default -> null;
        };
    }

    @Override
    public Value visitCreator(MiniJavaParser.CreatorContext ctx) {
        if (ctx.classCreatorRest() != null) {
            String className = ctx.createdName().identifier() == null ? null : ctx.createdName().identifier().getText();
            if (className == null || !classes.containsKey(className)) Value.fail(34);
            List<Value> args = new ArrayList<>();
            if (ctx.classCreatorRest().expressionList() != null) {
                for (MiniJavaParser.ExpressionContext expr : ctx.classCreatorRest().expressionList().expression()) args.add(visit(expr));
            }
            return createObject(className, args);
        }
        Value.TypeInfo baseType = ctx.createdName().primitiveType() != null
                ? Value.TypeInfo.primitive(primitiveBase(ctx.createdName().primitiveType().getText()))
                : Value.TypeInfo.classType(ctx.createdName().identifier().getText());
        if (baseType.isClass() && !classes.containsKey(baseType.className)) Value.fail(34);
        MiniJavaParser.ArrayCreatorRestContext rest = ctx.arrayCreatorRest();
        int totalDims = rest.LBRACK().size();
        Value.TypeInfo arrayType = Value.TypeInfo.arrayOf(baseType, totalDims);
        if (rest.arrayInitializer() != null) {
            return evalArrayInitializer(rest.arrayInitializer(), arrayType);
        }
        return createSizedArray(baseType, totalDims, rest.expression(), 0);
    }

    private Value createObject(String className, List<Value> args) {
        validateClassUsable(className);
        Value.ObjectValue object = new Value.ObjectValue(className);
        for (FieldDef field : allFields(className)) {
            validateType(field.type);
            object.fields.put(field.key(), env.defaultValue(field.type));
        }
        Value receiver = Value.objectValue(Value.TypeInfo.classType(className), object);
        invokeConstructor(className, receiver, args);
        return receiver;
    }

    private List<FieldDef> allFields(String className) {
        ClassDef def = classes.get(className);
        if (def == null) Value.fail(34);
        validateDeclaredFields(def);
        List<FieldDef> result = new ArrayList<>();
        if (def.parent != null) result.addAll(allFields(def.parent));
        result.addAll(def.fields);
        return result;
    }

    private void validateDeclaredFields(ClassDef def) {
        List<String> names = new ArrayList<>();
        for (FieldDef field : def.fields) {
            if (names.contains(field.name)) Value.fail(34);
            names.add(field.name);
        }
    }

    private void invokeConstructor(String className, Value receiver, List<Value> args) {
        invokeConstructor(className, receiver, args, new ArrayList<>());
    }

    private void invokeConstructor(String className, Value receiver, List<Value> args, List<String> constructorChain) {
        ClassDef def = classes.get(className);
        if (def == null) Value.fail(34);
        ConstructorDef constructor = resolveConstructor(def, args);
        if (constructor == null) {
            if (!def.constructors.isEmpty()) Value.fail(34);
            if (!args.isEmpty()) Value.fail(34);
            if (def.parent != null) invokeConstructor(def.parent, receiver, List.of(), constructorChain);
            initializeFields(def, receiver);
            return;
        }
        validateConstructorSignature(constructor, def.name);

        String signature = constructorSignature(className, constructor.parameterTypes);
        if (constructorChain.contains(signature)) Value.fail(34);
        constructorChain.add(signature);
        env.pushFrame();
        env.enterScope();
        Value previousThis = currentThis;
        String previousClass = currentClass;
        Value.TypeInfo previousReturnType = currentReturnType;
        int previousLoopDepth = loopDepth;
        currentThis = receiver;
        currentClass = className;
        currentReturnType = Value.VOID_TYPE;
        loopDepth = 0;
        try {
            for (int i = 0; i < args.size(); i++) {
                env.declare(constructor.parameterNames.get(i), constructor.parameterTypes.get(i), convertForCall(args.get(i), constructor.parameterTypes.get(i)));
            }
            MiniJavaParser.BlockContext body = constructor.body;
            int start = 0;
            boolean delegatedThis = false;
            if (!body.blockStatement().isEmpty() && isExplicitThisCall(body.blockStatement(0))) {
                invokeConstructor(className, receiver, methodArgs(body.blockStatement(0).statement().expression().methodCall().arguments()), constructorChain);
                start = 1;
                delegatedThis = true;
            } else if (!body.blockStatement().isEmpty() && isExplicitSuperCall(body.blockStatement(0))) {
                if (def.parent == null) Value.fail(34);
                invokeConstructor(def.parent, receiver, methodArgs(body.blockStatement(0).statement().expression().methodCall().arguments()), constructorChain);
                start = 1;
            } else if (def.parent != null) {
                invokeConstructor(def.parent, receiver, List.of(), constructorChain);
            }
            if (!delegatedThis) initializeFields(def, receiver);
            visitBlockStatements(body, start);
        } catch (ReturnSignal signal) {
            if (!signal.value.type.equals(Value.VOID_TYPE)) Value.fail(34);
        } finally {
            currentThis = previousThis;
            currentClass = previousClass;
            currentReturnType = previousReturnType;
            loopDepth = previousLoopDepth;
            env.exitScope();
            env.popFrame();
            constructorChain.remove(constructorChain.size() - 1);
        }
    }

    private String constructorSignature(String className, List<Value.TypeInfo> parameterTypes) {
        return className + "(" + parameterTypes + ")";
    }

    private ConstructorDef resolveConstructor(ClassDef def, List<Value> args) {
        List<ResolvedConstructor> matches = new ArrayList<>();
        for (ConstructorDef constructor : def.constructors) {
            if (constructor.parameterTypes.size() != args.size()) continue;
            int score = 0;
            boolean ok = true;
            for (int i = 0; i < args.size(); i++) {
                if (!isKnownType(constructor.parameterTypes.get(i))) {
                    ok = false;
                    break;
                }
                int cost = conversionCost(args.get(i), constructor.parameterTypes.get(i));
                if (cost < 0) {
                    ok = false;
                    break;
                }
                score += cost;
            }
            if (ok) matches.add(new ResolvedConstructor(constructor, score));
        }
        if (matches.isEmpty()) return null;
        matches.sort(Comparator.comparingInt(ResolvedConstructor::score));
        if (matches.size() > 1 && matches.get(0).score == matches.get(1).score) Value.fail(34);
        return matches.get(0).def;
    }

    private record ResolvedConstructor(ConstructorDef def, int score) {}

    private void initializeFields(ClassDef def, Value receiver) {
        Value previousThis = currentThis;
        String previousClass = currentClass;
        env.pushFrame();
        env.enterScope();
        currentThis = receiver;
        currentClass = def.name;
        try {
            for (FieldDef field : def.fields) {
                validateType(field.type);
                if (field.initializer != null) {
                    receiver.object().fields.put(field.key(), evalInitializer(field.initializer, field.type));
                }
            }
        } finally {
            currentThis = previousThis;
            currentClass = previousClass;
            env.exitScope();
            env.popFrame();
        }
    }

    private boolean isExplicitSuperCall(MiniJavaParser.BlockStatementContext stmt) {
        if (stmt.statement() == null || stmt.statement().expression() == null) return false;
        MiniJavaParser.ExpressionContext expr = stmt.statement().expression();
        return expr.methodCall() != null && expr.methodCall().SUPER() != null;
    }

    private boolean isExplicitThisCall(MiniJavaParser.BlockStatementContext stmt) {
        if (stmt.statement() == null || stmt.statement().expression() == null) return false;
        MiniJavaParser.ExpressionContext expr = stmt.statement().expression();
        return expr.methodCall() != null && expr.methodCall().THIS() != null;
    }

    private List<Value> methodArgs(MiniJavaParser.ArgumentsContext argsCtx) {
        List<Value> args = new ArrayList<>();
        if (argsCtx.expressionList() != null) {
            for (MiniJavaParser.ExpressionContext expr : argsCtx.expressionList().expression()) args.add(visit(expr));
        }
        return args;
    }

    private Value createSizedArray(Value.TypeInfo baseType, int totalDims, List<MiniJavaParser.ExpressionContext> sizes, int depth) {
        int length = visit(sizes.get(depth)).asInt();
        if (length < 0) Value.fail(34);
        Value.TypeInfo type = Value.TypeInfo.arrayOf(baseType, totalDims - depth);
        Value.TypeInfo elementType = type.elementType();
        List<Value> elements = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (depth + 1 < sizes.size()) {
                elements.add(createSizedArray(baseType, totalDims, sizes, depth + 1));
            } else {
                elements.add(env.defaultValue(elementType));
            }
        }
        return Value.arrayValue(type, elements);
    }

    private Value evalInitializer(MiniJavaParser.VariableInitializerContext ctx, Value.TypeInfo targetType) {
        if (ctx.arrayInitializer() != null) return evalArrayInitializer(ctx.arrayInitializer(), targetType);
        return env.castValue(visit(ctx.expression()), targetType);
    }

    private Value evalArrayInitializer(MiniJavaParser.ArrayInitializerContext ctx, Value.TypeInfo targetType) {
        if (!targetType.isArray()) Value.fail(34);
        Value.TypeInfo elementType = targetType.elementType();
        List<Value> elements = new ArrayList<>();
        for (MiniJavaParser.VariableInitializerContext init : ctx.variableInitializer()) {
            elements.add(evalInitializer(init, elementType));
        }
        return Value.arrayValue(targetType, elements);
    }

    private Env.LValue lvalue(MiniJavaParser.ExpressionContext ctx) {
        if (ctx.primary() != null && ctx.primary().identifier() != null) {
            String name = ctx.primary().identifier().getText();
            Env.LValue local = env.lvalueOrNull(name);
            if (local != null) return local;
            if (currentThis != null) return fieldLValue(currentThis, currentClass, name);
            Value.fail(34);
        }
        if (ctx.LBRACK() != null && ctx.bop == null) {
            return arrayElement(ctx.expression(0), ctx.expression(1));
        }
        if (ctx.bop != null && ctx.bop.getText().equals(".")) {
            return dotLValue(ctx);
        }
        Value.fail(34);
        return null;
    }

    private Value evalDot(MiniJavaParser.ExpressionContext ctx) {
        if (ctx.methodCall() != null) {
            if (isThisPrimary(ctx.expression(0))) {
                if (currentThis == null || currentClass == null) Value.fail(34);
                MiniJavaParser.MethodCallContext call = ctx.methodCall();
                if (call.identifier() == null) Value.fail(34);
                List<Value> args = methodArgs(call.arguments());
                return callInstanceMethod(currentThis, currentClass, call.identifier().getText(), args);
            }
            if (isSuperPrimary(ctx.expression(0))) {
                if (currentThis == null || currentClass == null) Value.fail(34);
                ClassDef def = classes.get(currentClass);
                if (def == null || def.parent == null) Value.fail(34);
                MiniJavaParser.MethodCallContext call = ctx.methodCall();
                if (call.identifier() == null) Value.fail(34);
                return callSuperInstanceMethod(currentThis, def.parent, call.identifier().getText(), methodArgs(call.arguments()));
            }
            Value receiver = visit(ctx.expression(0));
            if (!receiver.type.isClass()) Value.fail(34);
            MiniJavaParser.MethodCallContext call = ctx.methodCall();
            if (call.identifier() == null) Value.fail(34);
            List<Value> args = methodArgs(call.arguments());
            if (receiver.isNull()) Value.fail(34);
            return callInstanceMethod(receiver, receiver.type.className, call.identifier().getText(), args);
        }
        return dotLValue(ctx).get();
    }

    private boolean isSuperPrimary(MiniJavaParser.ExpressionContext ctx) {
        return ctx.primary() != null && ctx.primary().SUPER() != null;
    }

    private boolean isThisPrimary(MiniJavaParser.ExpressionContext ctx) {
        return ctx.primary() != null && ctx.primary().THIS() != null;
    }

    private Env.LValue dotLValue(MiniJavaParser.ExpressionContext ctx) {
        if (isThisPrimary(ctx.expression(0))) {
            if (currentThis == null || currentClass == null) Value.fail(34);
            return fieldLValue(currentThis, currentClass, ctx.identifier().getText());
        }
        if (isSuperPrimary(ctx.expression(0))) {
            if (currentThis == null || currentClass == null) Value.fail(34);
            ClassDef def = classes.get(currentClass);
            if (def == null || def.parent == null) Value.fail(34);
            return fieldLValue(currentThis, def.parent, ctx.identifier().getText());
        }
        Value receiver = visit(ctx.expression(0));
        if (!receiver.type.isClass()) Value.fail(34);
        return fieldLValue(receiver, receiver.type.className, ctx.identifier().getText());
    }

    private Env.LValue fieldLValue(Value receiver, String startClass, String fieldName) {
        FieldDef field = resolveField(startClass, fieldName);
        if (field == null) Value.fail(34);
        return new Env.LValue() {
            @Override
            public Value get() {
                if (receiver.isNull()) Value.fail(34);
                return receiver.object().fields.get(field.key());
            }

            @Override
            public Value set(Value value) {
                if (receiver.isNull()) Value.fail(34);
                Value cast = env.castValue(value, field.type);
                receiver.object().fields.put(field.key(), cast);
                return cast;
            }
        };
    }

    private FieldDef resolveField(String startClass, String fieldName) {
        validateClassUsable(startClass);
        for (String name = startClass; name != null; name = classes.get(name).parent) {
            ClassDef def = classes.get(name);
            if (def == null) Value.fail(34);
            FieldDef field = findDeclaredField(def, fieldName);
            if (field != null) return field;
        }
        return null;
    }

    private Value callInstanceMethod(Value receiver, String searchClass, String name, List<Value> args) {
        MethodDef method = resolveInstanceMethodOrNull(searchClass, name, args);
        if (method == null) Value.fail(34);
        return invokeSelectedInstanceMethod(receiver, method, args, true);
    }

    private Value callSuperInstanceMethod(Value receiver, String searchClass, String name, List<Value> args) {
        MethodDef method = resolveInstanceMethodOrNull(searchClass, name, args);
        if (method == null) Value.fail(34);
        return invokeSelectedInstanceMethod(receiver, method, args, false);
    }

    private Value invokeSelectedInstanceMethod(Value receiver, MethodDef selected, List<Value> args, boolean dynamicDispatch) {
        MethodDef target = dynamicDispatch ? findOverride(receiver.object().runtimeClass, selected) : selected;
        return invokeMethod(target, args, Value.objectValue(Value.TypeInfo.classType(target.owner), receiver.object()), target.owner);
    }

    private MethodDef findOverride(String runtimeClass, MethodDef selected) {
        for (String cls = runtimeClass; cls != null; cls = classes.get(cls).parent) {
            ClassDef def = classes.get(cls);
            if (def == null) Value.fail(34);
            for (MethodDef method : def.methods.getOrDefault(selected.name, List.of())) {
                if (method.parameterTypes.equals(selected.parameterTypes)) return method;
            }
            if (cls.equals(selected.owner)) break;
        }
        return selected;
    }

    private MethodDef resolveInstanceMethodOrNull(String searchClass, String name, List<Value> args) {
        List<MethodDef> candidates = visibleMethods(searchClass, name);
        List<ResolvedMethod> matches = new ArrayList<>();
        for (MethodDef def : candidates) {
            if (def.parameterTypes.size() != args.size()) continue;
            int score = 0;
            boolean ok = true;
            for (int i = 0; i < args.size(); i++) {
                if (!isKnownType(def.parameterTypes.get(i))) {
                    ok = false;
                    break;
                }
                int cost = conversionCost(args.get(i), def.parameterTypes.get(i));
                if (cost < 0) {
                    ok = false;
                    break;
                }
                score += cost;
            }
            if (ok) matches.add(new ResolvedMethod(def, score));
        }
        if (matches.isEmpty()) return null;
        matches.sort(Comparator.comparingInt(ResolvedMethod::score));
        if (matches.size() > 1 && matches.get(0).score == matches.get(1).score) Value.fail(34);
        return matches.get(0).def;
    }

    private List<MethodDef> visibleMethods(String searchClass, String name) {
        validateClassUsable(searchClass);
        List<MethodDef> result = new ArrayList<>();
        List<List<Value.TypeInfo>> hiddenInheritedSignatures = new ArrayList<>();
        for (String cls = searchClass; cls != null; cls = classes.get(cls).parent) {
            ClassDef def = classes.get(cls);
            if (def == null) Value.fail(34);
            List<MethodDef> declared = def.methods.getOrDefault(name, List.of());
            for (MethodDef method : declared) {
                if (hiddenInheritedSignatures.contains(method.parameterTypes)) continue;
                result.add(method);
            }
            for (MethodDef method : declared) {
                if (!hiddenInheritedSignatures.contains(method.parameterTypes)) {
                    hiddenInheritedSignatures.add(method.parameterTypes);
                }
            }
        }
        return result;
    }

    private Env.LValue arrayElement(MiniJavaParser.ExpressionContext arrayExpr, MiniJavaParser.ExpressionContext indexExpr) {
        Value arrayValue = visit(arrayExpr);
        if (!arrayValue.isArray()) Value.fail(34);
        int index = visit(indexExpr).asInt();
        Value.TypeInfo elementType = arrayValue.type.elementType();
        return new Env.LValue() {
            private Value.ArrayValue checkedArray() {
                if (arrayValue.isNull()) Value.fail(34);
                Value.ArrayValue array = arrayValue.array();
                if (index < 0 || index >= array.elements.size()) Value.fail(34);
                return array;
            }

            @Override
            public Value get() {
                return checkedArray().elements.get(index);
            }

            @Override
            public Value set(Value value) {
                Value cast = env.castValue(value, elementType);
                checkedArray().elements.set(index, cast);
                return cast;
            }
        };
    }

    private Value evalBinary(Value left, String op, Value right) {
        return switch (op) {
            case "*" -> integralBinary(left.asInt() * integral(right));
            case "/" -> {
                int r = integral(right);
                if (r == 0) Value.fail(34);
                yield Value.intValue(left.asInt() / r);
            }
            case "%" -> {
                int r = integral(right);
                if (r == 0) Value.fail(34);
                yield Value.intValue(left.asInt() % r);
            }
            case "+" -> plus(left, right);
            case "-" -> integralBinary(left.asInt() - integral(right));
            case "<<" -> integralBinary(left.asInt() << integral(right));
            case ">>" -> integralBinary(left.asInt() >> integral(right));
            case ">>>" -> integralBinary(left.asInt() >>> integral(right));
            case "<" -> compare(left, right, "<");
            case ">" -> compare(left, right, ">");
            case "<=" -> compare(left, right, "<=");
            case ">=" -> compare(left, right, ">=");
            case "==" -> equality(left, right, true);
            case "!=" -> equality(left, right, false);
            case "&" -> integralBinary(left.asInt() & integral(right));
            case "^" -> integralBinary(left.asInt() ^ integral(right));
            case "|" -> integralBinary(left.asInt() | integral(right));
            default -> {
                Value.fail(34);
                yield null;
            }
        };
    }

    private Value plus(Value left, Value right) {
        if (left.type.equals(Value.STRING) || right.type.equals(Value.STRING)) {
            return Value.stringValue(printable(left) + printable(right));
        }
        requireIntegral(left);
        requireIntegral(right);
        return Value.intValue(left.asInt() + right.asInt());
    }

    private Value compare(Value left, Value right, String op) {
        int l = integral(left);
        int r = integral(right);
        return Value.boolValue(switch (op) {
            case "<" -> l < r;
            case ">" -> l > r;
            case "<=" -> l <= r;
            case ">=" -> l >= r;
            default -> false;
        });
    }

    private Value equality(Value left, Value right, boolean equals) {
        boolean result;
        if (left.isNull() || right.isNull() || left.isArray() || right.isArray() || left.type.isClass() || right.type.isClass()) {
            if (!referenceComparable(left, right)) Value.fail(34);
            result = left.isNull() || right.isNull() ? left.isNull() && right.isNull() : left.value == right.value;
        } else if (left.type.isIntegral() && right.type.isIntegral()) {
            result = left.asInt() == right.asInt();
        } else if (left.type.equals(Value.BOOL) && right.type.equals(Value.BOOL)) {
            result = left.asBool() == right.asBool();
        } else if (left.type.equals(Value.STRING) && right.type.equals(Value.STRING)) {
            result = left.asString().equals(right.asString());
        } else {
            Value.fail(34);
            return null;
        }
        return Value.boolValue(equals == result);
    }

    private boolean referenceComparable(Value left, Value right) {
        if (left.type.equals(Value.NULL_TYPE) || right.type.equals(Value.NULL_TYPE)) {
            Value other = left.type.equals(Value.NULL_TYPE) ? right : left;
            return other.type.equals(Value.NULL_TYPE) || other.type.isReference();
        }
        if (left.type.isArray() || right.type.isArray()) {
            return left.type.isArray() && right.type.isArray() && left.type.equals(right.type);
        }
        if (left.type.isClass() && right.type.isClass()) {
            return isAssignableType(left.type, right.type) || isAssignableType(right.type, left.type);
        }
        return false;
    }

    private Value evalInstanceOf(Value value, Value.TypeInfo target) {
        if (!target.isClass()) Value.fail(34);
        if (!value.type.isClass()) Value.fail(34);
        if (!isAssignableType(value.type, target) && !isAssignableType(target, value.type)) Value.fail(34);
        if (value.isNull()) return Value.boolValue(false);
        return Value.boolValue(isSubclass(value.object().runtimeClass, target.className));
    }

    private Value castObject(Value value, Value.TypeInfo target) {
        if (!target.isClass()) Value.fail(34);
        if (value.type.equals(Value.NULL_TYPE)) return Value.nullValue(target);
        if (!value.type.isClass()) Value.fail(34);
        if (value.isNull()) {
            if (!isAssignableType(value.type, target) && !isAssignableType(target, value.type)) Value.fail(34);
            return Value.nullValue(target);
        }
        if (!isSubclass(value.object().runtimeClass, target.className)) Value.fail(34);
        return Value.objectValue(target, value.object());
    }

    private boolean isAssignableType(Value.TypeInfo from, Value.TypeInfo to) {
        if (from.equals(to)) return true;
        if (from.isClass() && to.isClass()) return isSubclass(from.className, to.className);
        return false;
    }

    private boolean isSubclass(String child, String parent) {
        List<String> path = new ArrayList<>();
        for (String cls = child; cls != null; cls = classes.get(cls).parent) {
            if (cls.equals(parent)) return true;
            if (path.contains(cls)) Value.fail(34);
            path.add(cls);
            if (!classes.containsKey(cls)) Value.fail(34);
        }
        return false;
    }

    private int inheritanceDistance(String child, String parent) {
        int distance = 0;
        for (String cls = child; cls != null; cls = classes.get(cls).parent) {
            if (cls.equals(parent)) return distance;
            distance++;
        }
        return 1000;
    }

    private String printable(Value value) {
        if (value.isNull()) return "null";
        if (value.isObject()) {
            MethodDef declaredToString = resolveInstanceMethodOrNull(value.type.className, "to_string", List.of());
            if (declaredToString != null && declaredToString.returnType.equals(Value.STRING)) {
                MethodDef target = findOverride(value.object().runtimeClass, declaredToString);
                return invokeMethod(target, List.of(), Value.objectValue(Value.TypeInfo.classType(target.owner), value.object()), target.owner).asString();
            }
            return value.object().runtimeClass;
        }
        if (value.isArray()) return arrayPrintable(value.array());
        return Value.printable(value);
    }

    private String arrayPrintable(Value.ArrayValue array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.elements.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(printable(array.elements.get(i)));
        }
        sb.append(']');
        return sb.toString();
    }

    private Value.TypeInfo parseType(MiniJavaParser.TypeTypeContext ctx) {
        int dims = ctx.LBRACK().size();
        Value.TypeInfo baseType;
        if (ctx.primitiveType() != null) {
            baseType = Value.TypeInfo.primitive(primitiveBase(ctx.primitiveType().getText()));
        } else {
            baseType = Value.TypeInfo.classType(ctx.identifier().getText());
        }
        return dims == 0 ? baseType : Value.TypeInfo.arrayOf(baseType, dims);
    }

    private Value.Base primitiveBase(String text) {
        return switch (text) {
            case "int" -> Value.Base.INT;
            case "char" -> Value.Base.CHAR;
            case "boolean" -> Value.Base.BOOL;
            case "string" -> Value.Base.STRING;
            default -> {
                Value.fail(34);
                yield null;
            }
        };
    }

    private int parseChar(String text) {
        String inner = text.substring(1, text.length() - 1);
        if (inner.length() == 1 && inner.charAt(0) != '\\') return inner.charAt(0);
        return switch (inner) {
            case "\\b" -> '\b';
            case "\\t" -> '\t';
            case "\\n" -> '\n';
            case "\\f" -> '\f';
            case "\\r" -> '\r';
            case "\\\"" -> '"';
            case "\\'" -> '\'';
            case "\\\\" -> '\\';
            default -> {
                Value.fail(34);
                yield 0;
            }
        };
    }

    private boolean isAssignment(String op) {
        return switch (op) {
            case "=", "+=", "-=", "*=", "/=", "&=", "^=", "|=", "<<=", ">>=", ">>>=", "%=" -> true;
            default -> false;
        };
    }

    private void requireIntegral(Value value) {
        if (!value.type.isIntegral()) Value.fail(34);
    }

    private int integral(Value value) {
        requireIntegral(value);
        return value.asInt();
    }

    private Value integralBinary(int value) {
        return Value.intValue(value);
    }

    private Value incremented(Value value, int delta) {
        int result = value.asInt() + delta;
        return value.type.equals(Value.CHAR) ? Value.charValue(result) : Value.intValue(result);
    }
}
