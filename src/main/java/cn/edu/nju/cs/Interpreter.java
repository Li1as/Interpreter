package cn.edu.nju.cs;

public class Interpreter extends MiniJavaParserBaseVisitor<Value> {

    @Override
    public Value visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Value visitPrimary(MiniJavaParser.PrimaryContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
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

        return error();
    }

    @Override
    public Value visitExpression(MiniJavaParser.ExpressionContext ctx) {
        if (ctx.primary() != null) {
            return visit(ctx.primary());
        }

        if (ctx.postfix != null) {
            return error();
        }

        if (ctx.prefix != null) {
            String op = ctx.prefix.getText();
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
            return error();
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

    private Value error() {
        System.out.println("Process exits with 34.");
        System.exit(34);
        return null;
    }
}