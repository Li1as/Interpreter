package cn.edu.nju.cs;

import java.util.*;

public class Env {

    public static final class Var {
        public final String name;
        public final Value.Type type;
        public Value value;

        public Var(String name, Value.Type type, Value value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }
    }

    private static final class Scope {
        final int depth;
        final LinkedHashMap<String, Var> vars = new LinkedHashMap<>();

        Scope(int depth) {
            this.depth = depth;
        }
    }

    private final Deque<Scope> scopes = new ArrayDeque<>();

    public void enterScope() {
        // System.err.printf("enterScope: %d%n", scopes.size());
        scopes.push(new Scope(scopes.size()));
    }

    private String typeToString(Value.Type t) {
        return switch (t) {
            case INT -> "int";
            case CHAR -> "char";
            case BOOL -> "boolean";
            case STRING -> "string";
            case VOID -> "void";
        };
    }

    private String valueToString(Value v) {
        return switch (v.type) {
            case INT -> String.valueOf(v.asInt());
            case CHAR -> String.valueOf((char) (v.asInt() & 0xFF));
            case BOOL -> String.valueOf(v.asBool());
            case STRING -> v.asString();
            default -> "";
        };
    }

    private String formatLine(int depth, Var v) {
        return "Scope " + depth + ": " + v.name + ": (" + typeToString(v.type) + ") " + valueToString(v.value);
    }

    public void exitScope() {
        // System.err.printf("exitScope: %d%n", scopes.size() - 1);
        if (scopes.isEmpty()) {
            error();
        }
        Scope scope = scopes.pop();

        List<String> names = new ArrayList<>(scope.vars.keySet());
        Collections.sort(names);
        for (String name : names) {
            Var v = scope.vars.get(name);
            System.out.println(formatLine(scope.depth, v));
        }
    }

    public void declare(String name, Value.Type type, Value value) {
        if (scopes.isEmpty()) {
            error();
        }
        Scope scope = scopes.peek();
        if (scope.vars.containsKey(name)) {
            error();
        }
        scope.vars.put(name, new Var(name, type, castValue(value, type)));
    }

    public static Value defaultValue(Value.Type type) {
        return switch (type) {
            case INT -> new Value(Value.Type.INT, 0);
            case CHAR -> new Value(Value.Type.CHAR, (byte) 0);
            case BOOL -> new Value(Value.Type.BOOL, false);
            case STRING -> new Value(Value.Type.STRING, "");
            case VOID -> Value.VOID;
        };
    }

    private static Value castValue(Value value, Value.Type type) {
        return switch (type) {
            case INT -> {
                if (value.type == Value.Type.INT || value.type == Value.Type.CHAR) {
                    yield new Value(Value.Type.INT, value.asInt());
                }
                error();
                yield null; // unreachable
            }
            case CHAR -> {
                if (value.type == Value.Type.INT || value.type == Value.Type.CHAR) {
                    yield new Value(Value.Type.CHAR, (byte) value.asInt());
                }
                error();
                yield null; // unreachable
            }
            case BOOL -> {
                if (value.type == Value.Type.BOOL) {
                    yield value;
                }
                error();
                yield null; // unreachable
            }
            case STRING -> {
                if (value.type == Value.Type.STRING) {
                    yield value;
                }
                error();
                yield null; // unreachable
            }
            default -> {
                error();
                yield null; // unreachable
            }
        };
    }

    public Value load(String name) {
        for (Scope scope : scopes) {
            Var v = scope.vars.get(name);
            if (v != null) return v.value;
        }
        return error();
    }

    public Value assign(String name, Value value) {
        for (Scope scope : scopes) {
            Var v = scope.vars.get(name);
            if (v != null) {
                v.value = castValue(value, v.type);
                return v.value;
            }
        }
        return error();
    }

    private String valueToConcatString(Value v) {
        return switch (v.type) {
            case STRING -> v.asString();
            case CHAR -> String.valueOf((char) (v.asInt() & 0xFF));
            case INT -> String.valueOf(v.asInt());
            case BOOL -> String.valueOf(v.asBool());
            default -> "";
        };
    }

    public Value applyAssignment(String name, String bop, Value rvalue) {
        for (Scope scope : scopes) {
            Var v = scope.vars.get(name);
            if (v != null) {
                Value lvalue = v.value;
                Value result;
                switch (bop) {
                    case "=" -> result = castValue(rvalue, v.type);
                    case "+=" -> {
                        if (v.type == Value.Type.STRING) {
                            result = new Value(Value.Type.STRING, lvalue.asString() + valueToConcatString(rvalue));
                        } else {
                            int x = lvalue.asInt() + rvalue.asInt();
                            result = castValue(new Value(Value.Type.INT, x), v.type);
                        }
                    }
                    case "-=" -> {
                        int x = lvalue.asInt() - rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "*=" -> {
                        int x = lvalue.asInt() * rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "/=" -> {
                        if (rvalue.asInt() == 0) {
                            error();
                        }
                        int x = lvalue.asInt() / rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "&=" -> {
                        int x = lvalue.asInt() & rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "^=" -> {
                        int x = lvalue.asInt() ^ rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "|=" -> {
                        int x = lvalue.asInt() | rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "<<=" -> {
                        int x = lvalue.asInt() << rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case ">>=" -> {
                        int x = lvalue.asInt() >> rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case ">>>=" -> {
                        int x = lvalue.asInt() >>> rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    case "%=" -> {
                        if (rvalue.asInt() == 0) {
                            error();
                        }
                        int x = lvalue.asInt() % rvalue.asInt();
                        result = castValue(new Value(Value.Type.INT, x), v.type);
                    }
                    default -> {
                        error();
                        result = null; // unreachable
                    }
                }
                v.value = result;
                return v.value;
            }
        }
        return error();
    }

    private static Value error() {
        System.out.println("Process exits with 34.");
        System.exit(34);
        return null; // unreachable
    }
}