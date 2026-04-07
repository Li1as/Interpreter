package cn.edu.nju.cs;

public class Interpreter extends MiniJavaParserBaseVisitor<Value> {

    private final Env env = new Env();
    private int loopDepth = 0;

    private static class BreakSignal extends RuntimeException {}
    private static class ContinuseSignal extends RuntimeException {}

    @Override
    public Value visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
        // System.err.println("CompilationUnit");
        return visit(ctx.block());
    }

    @Override
    public Value visitBlock(MiniJavaParser.BlockContext ctx) {
        // System.err.println("Block");
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
        // System.err.println("BlockStatement");
        if (ctx.localVariableDeclaration() != null) {
            return visit(ctx.localVariableDeclaration());
        }
        return visit(ctx.statement());
    }

    @Override
    public Value visitLocalVariableDeclaration(MiniJavaParser.LocalVariableDeclarationContext ctx) {
        // System.err.println("LocalVariableDeclaration");
        Value.Type type;
        switch (ctx.primitiveType().getText()) {
            case "int" -> type = Value.Type.INT;
            case "char" -> type = Value.Type.CHAR;
            case "boolean" -> type = Value.Type.BOOL;
            case "string" -> type = Value.Type.STRING;
            default -> {
                error();
                type = null; // unreachable
            }
        };
        String name = ctx.identifier().getText();
        if (ctx.expression() != null) {
            Value value = visit(ctx.expression());
            env.declare(name, type, value);
        } else {
            env.declare(name, type, Env.defaultValue(type));
        }
        return Value.VOID;
    }

    @Override
    public Value visitPrimary(MiniJavaParser.PrimaryContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        }
        else if (ctx.identifier() != null) {
            String name = ctx.identifier().getText();
            return env.load(name);
        }
        return visit(ctx.expression());
    }

    @Override
    public Value visitLiteral(MiniJavaParser.LiteralContext ctx) {
        String text = ctx.getText();
        if (ctx.BOOL_LITERAL() != null) {
            return new Value(Value.Type.BOOL, Boolean.parseBoolean(text));
        }

        if (ctx.STRING_LITERAL() != null) {
            return new Value(Value.Type.STRING, text.substring(1, text.length() - 1));
        }

        if (ctx.CHAR_LITERAL() != null) {
            String inner = text.substring(1, text.length() - 1);
            char c;
            if (inner.length() == 1 && inner.charAt(0) != '\\') {
                c = inner.charAt(0);
            } else if (inner.equals("\\b")) {
                c = '\b';
            } else if (inner.equals("\\t")) {
                c = '\t';
            } else if (inner.equals("\\n")) {
                c = '\n';
            } else if (inner.equals("\\f")) {
                c = '\f';
            } else if (inner.equals("\\r")) {
                c = '\r';
            } else if (inner.equals("\\\"")) {
                c = '\"';
            } else if (inner.equals("\\'")) {
                c = '\'';
            } else if (inner.equals("\\\\")) {
                c = '\\';
            } else {
                return error();
            }
            return new Value(Value.Type.CHAR, (byte) c);
        }

        if (ctx.DECIMAL_LITERAL() != null) {
            int parsedInt = new java.math.BigInteger(text.replace("_", "")).intValue();
            return new Value(Value.Type.INT, parsedInt);
        }

        if (ctx.NULL_LITERAL() != null) {
            return new Value(Value.Type.VOID, 0);
        }

        return error();
    }

    @Override
    public Value visitExpression(MiniJavaParser.ExpressionContext ctx) {
        // System.err.println("Expression: " + ctx.getText());
        if (ctx.primary() != null) {
            return visit(ctx.primary());
        }

        if (ctx.postfix != null) {
            String op = ctx.postfix.getText();
            if (!(ctx.expression(0).primary() != null && ctx.expression(0).primary().identifier() != null)) {
                return error();
            }
            String name = ctx.expression(0).primary().identifier().getText();
            Value v = env.load(name);
            if (!(v.type == Value.Type.INT || v.type == Value.Type.CHAR)) {
                return error();
            }
            int after = v.asInt() + (op.equals("++") ? 1 : -1);
            env.assign(name, new Value(Value.Type.INT, after));
            return v;
        }

        if (ctx.prefix != null) {
            String op = ctx.prefix.getText();
            if (op.equals("++") || op.equals("--")) {
                if (!(ctx.expression(0).primary() != null && ctx.expression(0).primary().identifier() != null)) {
                    return error();
                }
                String name = ctx.expression(0).primary().identifier().getText();
                Value v = env.load(name);
                if (!(v.type == Value.Type.INT || v.type == Value.Type.CHAR)) {
                    return error();
                }
                int after = v.asInt() + (op.equals("++") ? 1 : -1);
                Value result = env.assign(name, new Value(Value.Type.INT, after));
                return result;
            }
            Value v = visit(ctx.expression(0));
            switch (op) {
                case "+" -> {
                    return new Value(Value.Type.INT, v.asInt());
                }
                case "-" -> {
                    return new Value(Value.Type.INT, -v.asInt());
                }
                case "~" -> {
                    return new Value(Value.Type.INT, ~v.asInt());
                }
                case "not" -> {
                    return new Value(Value.Type.BOOL, !v.asBool());
                }
                default -> {
                    return error();
                }
            }
        }

        if (ctx.primitiveType() != null) {
            String tp = ctx.primitiveType().getText();
            Value v = visit(ctx.expression(0));
            switch (tp) {
                case "int" -> {
                    return new Value(Value.Type.INT, v.asInt());
                }
                case "char" -> {
                    return new Value(Value.Type.CHAR, (byte) v.asInt());
                }
                default -> {
                    return error();
                }
            }
        }

        if (ctx.bop == null) {
            return error();
        }

        String bop = ctx.bop.getText();
        if (bop.equals("=") || bop.equals("+=") || bop.equals("-=") || bop.equals("*=") || bop.equals("/=") || bop.equals("&=") || bop.equals("^=") || bop.equals("|=") || bop.equals("<<=") || bop.equals(">>=") || bop.equals(">>>=") || bop.equals("%=")) {
            if (!(ctx.expression(0).primary() != null && ctx.expression(0).primary().identifier() != null)) {
                return error();
            }
            String name = ctx.expression(0).primary().identifier().getText();
            Value rvalue = visit(ctx.expression(1));
            return env.applyAssignment(name, bop, rvalue);
        }
        Value v1 = visit(ctx.expression(0));
        if (bop.equals("?")) {
            if (v1.asBool()) {
                return visit(ctx.expression(1));
            } else {
                return visit(ctx.expression(2));
            }
        }
        if (bop.equals("and")) {
            if (!v1.asBool()) {
                return new Value(Value.Type.BOOL, false);
            }
            return new Value(Value.Type.BOOL, visit(ctx.expression(1)).asBool());
        }
        if (bop.equals("or")) {
            if (v1.asBool()) {
                return new Value(Value.Type.BOOL, true);
            }
            return new Value(Value.Type.BOOL, visit(ctx.expression(1)).asBool());
        }
        Value v2 = visit(ctx.expression(1));
        switch(bop){
            case "*" -> {
                return new Value(Value.Type.INT, v1.asInt() * v2.asInt());
            }
            case "/" -> {
                if (v2.asInt() == 0) {
                    return error();
                }
                return new Value(Value.Type.INT, v1.asInt() / v2.asInt());
            }
            case "%" -> {
                if (v2.asInt() == 0) {
                    return error();
                }
                return new Value(Value.Type.INT, v1.asInt() % v2.asInt());
            }
            case "+" -> {
                if (v1.type == Value.Type.STRING || v2.type == Value.Type.STRING) {
                    String s1 = v1.type == Value.Type.CHAR ? String.valueOf((char) (v1.asInt() & 0xFF)) : v1.asString();
                    String s2 = v2.type == Value.Type.CHAR ? String.valueOf((char) (v2.asInt() & 0xFF)) : v2.asString();
                    return new Value(Value.Type.STRING, s1 + s2);
                }
                return new Value(Value.Type.INT, v1.asInt() + v2.asInt());
            }
            case "-" -> {
                return new Value(Value.Type.INT, v1.asInt() - v2.asInt());
            }
            case "<<" -> {
                return new Value(Value.Type.INT, v1.asInt() << v2.asInt());
            }
            case ">>" -> {
                return new Value(Value.Type.INT, v1.asInt() >> v2.asInt());
            }
            case ">>>" -> {
                return new Value(Value.Type.INT, v1.asInt() >>> v2.asInt());
            }
            case "<=" -> {
                return new Value(Value.Type.BOOL, v1.asInt() <= v2.asInt());
            }
            case ">=" -> {
                return new Value(Value.Type.BOOL, v1.asInt() >= v2.asInt());
            }
            case ">" -> {
                return new Value(Value.Type.BOOL, v1.asInt() > v2.asInt());
            }
            case "<" -> {
                return new Value(Value.Type.BOOL, v1.asInt() < v2.asInt());
            }
            case "==" -> {
                if (v1.type == v2.type && v1.type == Value.Type.BOOL) {
                    return new Value(Value.Type.BOOL, v1.asBool() == v2.asBool());
                }
                if (v1.type == v2.type && v1.type == Value.Type.STRING) {
                    return new Value(Value.Type.BOOL, v1.asString().equals(v2.asString()));
                }
                return new Value(Value.Type.BOOL, v1.asInt() == v2.asInt());
            }
            case "!=" -> {
                if (v1.type == v2.type && v1.type == Value.Type.BOOL) {
                    return new Value(Value.Type.BOOL, v1.asBool() != v2.asBool());
                }
                if (v1.type == v2.type && v1.type == Value.Type.STRING) {
                    return new Value(Value.Type.BOOL, !v1.asString().equals(v2.asString()));
                }
                return new Value(Value.Type.BOOL, v1.asInt() != v2.asInt());
            }
            case "&" -> {
                return new Value(Value.Type.INT, v1.asInt() & v2.asInt());
            }
            case "^" -> {
                return new Value(Value.Type.INT, v1.asInt() ^ v2.asInt());
            }
            case "|" -> {
                return new Value(Value.Type.INT, v1.asInt() | v2.asInt());
            }
            default -> {return error();}
        }

    }

    @Override
    public Value visitStatement(MiniJavaParser.StatementContext ctx) {
        if (ctx.block() != null) {
            return visit(ctx.block());
        }
        if (ctx.IF() != null) {
            Value cond = visit(ctx.parExpression());
            if (cond.type != Value.Type.BOOL) {
                return error();
            }
            if (cond.asBool()) {
                return visit(ctx.statement(0));
            } else if (ctx.ELSE() != null) {
                return visit(ctx.statement(1));
            } else {
                return Value.VOID;
            }
        }
        if (ctx.FOR() != null) {
            MiniJavaParser.ForControlContext forControl = ctx.forControl();
            env.enterScope();
            loopDepth++;
            try {
                if (forControl.forInit() != null) {
                    visit(forControl.forInit());
                }
                while (true) {
                    if (forControl.expression() != null) {
                        Value cond = visit(forControl.expression());
                        if (cond.type != Value.Type.BOOL) {
                            return error();
                        }
                        if (!cond.asBool()) {
                            break;
                        }
                    }
                    try {
                        visit(ctx.statement(0));
                    } catch (BreakSignal e) {
                        break;
                    } catch (ContinuseSignal e) {
                        // do nothing
                    }
                    if (forControl.forUpdate != null) {
                        for (MiniJavaParser.ExpressionContext expr : forControl.forUpdate.expression()) {
                            visit(expr);
                        }
                    }
                }
                return Value.VOID;
            } finally {
                loopDepth--;
                env.exitScope();
            }
        }
        if (ctx.WHILE() != null) {
            loopDepth++;
            try {
                while (true) {
                    Value cond = visit(ctx.parExpression());
                    if (cond.type != Value.Type.BOOL) {
                        return error();
                    }
                    if (!cond.asBool()) {
                        break;
                    }
                    try {
                        visit(ctx.statement(0));
                    } catch (BreakSignal e) {
                        break;
                    } catch (ContinuseSignal e) {
                        continue;
                    }
                }
                return Value.VOID;
            } finally {
                loopDepth--;
            }
        }
        if (ctx.BREAK() != null) {
            if (loopDepth == 0) {
                return error();
            }
            throw new BreakSignal();
        }
        if (ctx.CONTINUE() != null) {
            if (loopDepth == 0) {
                return error();
            }
            throw new ContinuseSignal();
        }
        if (ctx.expression() != null) {
            return visit(ctx.expression());
        }
        if (ctx.SEMI() != null) {
            return Value.VOID;
        }
        return error();
    }

    @Override
    public Value visitForInit(MiniJavaParser.ForInitContext ctx) {
        if (ctx.localVariableDeclaration() != null) {
            return visit(ctx.localVariableDeclaration());
        }
        if (ctx.expressionList() != null) {
            for (MiniJavaParser.ExpressionContext expr : ctx.expressionList().expression()) {
                visit(expr);
            }
        }
        return Value.VOID;
    }

    @Override
    public Value visitParExpression(MiniJavaParser.ParExpressionContext ctx) {
        return visit(ctx.expression());
    }

    private Value error() {
        System.out.println("Process exits with 34.");
        System.exit(34);
        return null; // unreachable
    }
}