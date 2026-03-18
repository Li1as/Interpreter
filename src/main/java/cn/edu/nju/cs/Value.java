package cn.edu.nju.cs;

public class Value {
    public enum Type {
        INT, CHAR, BOOL, STRING
    }

    public final Type type;
    public final Object value;

    public Value(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public int asInt() {
        if (type == Type.INT) return (int) value;
        if (type == Type.CHAR) return (byte) value; // sign extension
        error();
        return 0; // unreachable
    }

    public boolean asBool() {
        if (type == Type.BOOL) return (boolean) value;
        error();
        return false; // unreachable
    }

    public String asString() {
        return String.valueOf(value);
    }

    private void error() {
        System.out.println("Process exits with 34.");
        System.exit(34);
    }
}