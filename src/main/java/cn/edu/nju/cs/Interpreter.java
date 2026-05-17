package cn.edu.nju.cs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Interpreter extends MiniJavaParserBaseVisitor<Value> {
    private final Env env = new Env();
    private final Map<String, List<MethodDef>> methods = new HashMap<>();
    private int loopDepth = 0;
    private Value.TypeInfo currentReturnType = Value.VOID_TYPE;

    private record MethodDef(
            String name,
            Value.TypeInfo returnType,
            List<Value.TypeInfo> parameterTypes,
            List<String> parameterNames,
            MiniJavaParser.BlockContext body
    ) {}

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
        for (MiniJavaParser.MethodDeclarationContext method : ctx.methodDeclaration()) {
            MethodDef def = visitMD(method);
            List<MethodDef> overloads = methods.computeIfAbsent(def.name, ignored -> new ArrayList<>());
            overloads.add(def);
        }
        Value result = callMethod("main", List.of(), true);
        System.out.println("Process exits with " + result.asInt() + ".");
        System.exit(result.asInt());
        return Value.VOID;
    }

    private MethodDef visitMD(MiniJavaParser.MethodDeclarationContext ctx) {
        Value.TypeInfo returnType = ctx.VOID() != null ? Value.VOID_TYPE : parseType(ctx.typeType());
        List<Value.TypeInfo> parameterTypes = new ArrayList<>();
        List<String> parameterNames = new ArrayList<>();
        Set<String> seenParameterNames = new HashSet<>();
        if (ctx.formalParameters().formalParameterList() != null) {
            for (MiniJavaParser.FormalParameterContext param : ctx.formalParameters().formalParameterList().formalParameter()) {
                parameterTypes.add(parseType(param.typeType()));
                String parameterName = param.identifier().getText();
                if (!seenParameterNames.add(parameterName)) Value.fail(34);
                parameterNames.add(parameterName);
            }
        }
        return new MethodDef(ctx.identifier().getText(), returnType, parameterTypes, parameterNames, ctx.methodBody);
    }

    @Override
    public Value visitBlock(MiniJavaParser.BlockContext ctx) {
        env.enterScope();
        try {
            for (MiniJavaParser.BlockStatementContext stmt : ctx.blockStatement()) {
                visit(stmt);
            }
            return Value.VOID;
        } finally {
            env.exitScope();
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
        if (ctx.identifier() != null) return env.load(ctx.identifier().getText());
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
        if (ctx.methodCall() != null) return visitMethodCall(ctx.methodCall());
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

        if (ctx.typeType() != null) {
            Value.TypeInfo type = parseType(ctx.typeType());
            if (type.isArray() || type.equals(Value.BOOL) || type.equals(Value.STRING)) Value.fail(34);
            Value value = visit(ctx.expression(0));
            requireIntegral(value);
            return type.equals(Value.INT) ? Value.intValue(value.asInt()) : Value.charValue(value.asInt());
        }

        if (ctx.LBRACK() != null && ctx.bop == null) {
            return arrayElement(ctx.expression(0), ctx.expression(1)).get();
        }

        if (ctx.bop == null) Value.fail(34);
        String op = ctx.bop.getText();
        if (isAssignment(op)) {
            Env.LValue target = lvalue(ctx.expression(0));
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
            throw new ReturnSignal(Env.castReturnValue(value, currentReturnType));
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
        env.pushFrame();
        env.enterScope();
        Value.TypeInfo previousReturnType = currentReturnType;
        int previousLoopDepth = loopDepth;
        currentReturnType = def.returnType;
        loopDepth = 0;
        try {
            for (int i = 0; i < args.size(); i++) {
                env.declare(def.parameterNames.get(i), def.parameterTypes.get(i), convertForCall(args.get(i), def.parameterTypes.get(i)));
            }
            try {
                visitMethodBody(def.body);
            } catch (ReturnSignal signal) {
                return def.returnType.equals(Value.CHAR) ? signal.value.markCharMethodReturn() : signal.value;
            }
            if (!def.returnType.equals(Value.VOID_TYPE)) Value.fail(34);
            return Value.VOID;
        } finally {
            currentReturnType = previousReturnType;
            loopDepth = previousLoopDepth;
            env.exitScope();
            env.popFrame();
        }
    }

    private Value visitMethodBody(MiniJavaParser.BlockContext ctx) {
        for (MiniJavaParser.BlockStatementContext stmt : ctx.blockStatement()) {
            visit(stmt);
        }
        return Value.VOID;
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
            boolean hasVoidMain = false;
            for (MethodDef def : candidates) {
                if (!def.name.equals("main") || !def.parameterTypes.isEmpty()) continue;
                if (def.returnType.equals(Value.VOID_TYPE)) {
                    hasVoidMain = true;
                } else if (def.returnType.equals(Value.INT)) {
                    if (entry != null) Value.fail(34);
                    entry = def;
                }
            }
            if (hasVoidMain) Value.fail(34);
            return entry;
        }

        List<ResolvedMethod> matches = new ArrayList<>();
        for (MethodDef def : candidates) {
            if (def.parameterTypes.size() != args.size()) continue;
            for (MethodDef existing : candidates) {
                if (existing == def) break;
                if (existing.parameterTypes.equals(def.parameterTypes)) Value.fail(34);
            }
            int score = 0;
            boolean ok = true;
            for (int i = 0; i < args.size(); i++) {
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
        if (value.type.equals(Value.NULL_TYPE) && target.isArray()) return 1;
        return -1;
    }

    private Value convertForCall(Value value, Value.TypeInfo target) {
        if (value.type.equals(target)) return value;
        if (value.type.equals(Value.CHAR) && target.equals(Value.INT)) return Value.intValue(value.asInt());
        if (value.type.equals(Value.NULL_TYPE) && target.isArray()) return Value.nullValue(target);
        Value.fail(34);
        return null;
    }

    @Override
    public Value visitMethodCall(MiniJavaParser.MethodCallContext ctx) {
        String name = ctx.identifier().getText();
        List<Value> args = new ArrayList<>();
        if (ctx.arguments().expressionList() != null) {
            for (MiniJavaParser.ExpressionContext expr : ctx.arguments().expressionList().expression()) args.add(visit(expr));
        }
        return callMethod(name, args, false);
    }

    private Value tryBuiltIn(String name, List<Value> args) {
        return switch (name) {
            case "print" -> {
                if (args.size() != 1) Value.fail(34);
                if (args.get(0).type.equals(Value.VOID_TYPE)) Value.fail(34);
                System.out.print(Value.printable(args.get(0)));
                yield Value.VOID;
            }
            case "println" -> {
                if (args.isEmpty()) {
                    System.out.println();
                } else if (args.size() == 1) {
                    if (args.get(0).type.equals(Value.VOID_TYPE)) Value.fail(34);
                    System.out.println(Value.printable(args.get(0)));
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
        Value.Base base = primitiveBase(ctx.createdName().primitiveType().getText());
        MiniJavaParser.ArrayCreatorRestContext rest = ctx.arrayCreatorRest();
        int totalDims = rest.LBRACK().size();
        Value.TypeInfo arrayType = Value.TypeInfo.array(base, totalDims);
        if (rest.arrayInitializer() != null) {
            return evalArrayInitializer(rest.arrayInitializer(), arrayType);
        }
        return createSizedArray(base, totalDims, rest.expression(), 0);
    }

    private Value createSizedArray(Value.Base base, int totalDims, List<MiniJavaParser.ExpressionContext> sizes, int depth) {
        int length = visit(sizes.get(depth)).asInt();
        if (length < 0) Value.fail(34);
        Value.TypeInfo type = Value.TypeInfo.array(base, totalDims - depth);
        Value.TypeInfo elementType = type.elementType();
        List<Value> elements = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (depth + 1 < sizes.size()) {
                elements.add(createSizedArray(base, totalDims, sizes, depth + 1));
            } else {
                elements.add(env.defaultValue(elementType));
            }
        }
        return Value.arrayValue(type, elements);
    }

    private Value evalInitializer(MiniJavaParser.VariableInitializerContext ctx, Value.TypeInfo targetType) {
        if (ctx.arrayInitializer() != null) return evalArrayInitializer(ctx.arrayInitializer(), targetType);
        return Env.castValue(visit(ctx.expression()), targetType);
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
            return env.lvalue(ctx.primary().identifier().getText());
        }
        if (ctx.LBRACK() != null && ctx.bop == null) {
            return arrayElement(ctx.expression(0), ctx.expression(1));
        }
        Value.fail(34);
        return null;
    }

    private Env.LValue arrayElement(MiniJavaParser.ExpressionContext arrayExpr, MiniJavaParser.ExpressionContext indexExpr) {
        Value arrayValue = visit(arrayExpr);
        if (arrayValue.isNull()) Value.fail(34);
        if (!arrayValue.isArray()) Value.fail(34);
        int index = visit(indexExpr).asInt();
        Value.ArrayValue array = arrayValue.array();
        if (index < 0 || index >= array.elements.size()) Value.fail(34);
        Value.TypeInfo elementType = array.type.elementType();
        return new Env.LValue() {
            @Override
            public Value get() {
                return array.elements.get(index);
            }

            @Override
            public Value set(Value value) {
                Value cast = Env.castValue(value, elementType);
                array.elements.set(index, cast);
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
            return Value.stringValue(Value.printable(left) + Value.printable(right));
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
        if (left.isNull() || right.isNull() || left.isArray() || right.isArray()) {
            if (!((left.isNull() || left.isArray()) && (right.isNull() || right.isArray()))) Value.fail(34);
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

    private Value.TypeInfo parseType(MiniJavaParser.TypeTypeContext ctx) {
        Value.Base base = primitiveBase(ctx.primitiveType().getText());
        int dims = ctx.LBRACK().size();
        return dims == 0 ? Value.TypeInfo.primitive(base) : Value.TypeInfo.array(base, dims);
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
