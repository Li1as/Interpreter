package cn.edu.nju.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Value {
    public enum Base {
        INT, CHAR, BOOL, STRING, VOID, NULL
    }

    public static final class TypeInfo {
        public final Base base;
        public final int dimensions;

        private TypeInfo(Base base, int dimensions) {
            this.base = base;
            this.dimensions = dimensions;
        }

        public static TypeInfo primitive(Base base) {
            return new TypeInfo(base, 0);
        }

        public static TypeInfo array(Base base, int dimensions) {
            if (dimensions <= 0) fail(34);
            return new TypeInfo(base, dimensions);
        }

        public TypeInfo elementType() {
            if (!isArray()) fail(34);
            return dimensions == 1 ? primitive(base) : array(base, dimensions - 1);
        }

        public boolean isArray() {
            return dimensions > 0;
        }

        public boolean isIntegral() {
            return dimensions == 0 && (base == Base.INT || base == Base.CHAR);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TypeInfo other)) return false;
            return base == other.base && dimensions == other.dimensions;
        }

        @Override
        public int hashCode() {
            return Objects.hash(base, dimensions);
        }

        @Override
        public String toString() {
            String name = switch (base) {
                case INT -> "int";
                case CHAR -> "char";
                case BOOL -> "boolean";
                case STRING -> "string";
                case VOID -> "void";
                case NULL -> "null";
            };
            return name + "[]".repeat(dimensions);
        }
    }

    public static final class ArrayValue {
        public final TypeInfo type;
        public final List<Value> elements;

        public ArrayValue(TypeInfo type, List<Value> elements) {
            if (!type.isArray()) fail(34);
            this.type = type;
            this.elements = new ArrayList<>(elements);
        }
    }

    public static final TypeInfo INT = TypeInfo.primitive(Base.INT);
    public static final TypeInfo CHAR = TypeInfo.primitive(Base.CHAR);
    public static final TypeInfo BOOL = TypeInfo.primitive(Base.BOOL);
    public static final TypeInfo STRING = TypeInfo.primitive(Base.STRING);
    public static final TypeInfo VOID_TYPE = TypeInfo.primitive(Base.VOID);
    public static final TypeInfo NULL_TYPE = TypeInfo.primitive(Base.NULL);

    public final TypeInfo type;
    public final Object value;
    public final boolean decimalLiteral;
    public final boolean charMethodReturn;

    public Value(TypeInfo type, Object value) {
        this(type, value, false, false);
    }

    public Value(TypeInfo type, Object value, boolean decimalLiteral) {
        this(type, value, decimalLiteral, false);
    }

    public Value(TypeInfo type, Object value, boolean decimalLiteral, boolean charMethodReturn) {
        this.type = type;
        this.value = value;
        this.decimalLiteral = decimalLiteral;
        this.charMethodReturn = charMethodReturn;
    }

    public static Value intValue(int value) {
        return new Value(INT, value);
    }

    public static Value decimalInt(int value) {
        return new Value(INT, value, true);
    }

    public static Value charValue(int value) {
        return new Value(CHAR, (byte) value);
    }

    public Value markCharMethodReturn() {
        if (!type.equals(CHAR)) return this;
        return new Value(type, value, decimalLiteral, true);
    }

    public static Value boolValue(boolean value) {
        return new Value(BOOL, value);
    }

    public static Value stringValue(String value) {
        return new Value(STRING, value);
    }

    public static Value arrayValue(TypeInfo type, List<Value> elements) {
        return new Value(type, new ArrayValue(type, elements));
    }

    public static Value nullValue(TypeInfo type) {
        if (!type.isArray()) fail(34);
        return new Value(type, null);
    }

    public static final Value VOID = new Value(VOID_TYPE, null);
    public static final Value NULL = new Value(NULL_TYPE, null);

    public boolean isNull() {
        return value == null && (type.base == Base.NULL || type.isArray());
    }

    public boolean isArray() {
        return type.isArray();
    }

    public int asInt() {
        if (type.base == Base.INT && type.dimensions == 0) return (int) value;
        if (type.base == Base.CHAR && type.dimensions == 0) return (byte) value;
        fail(34);
        return 0;
    }

    public boolean asBool() {
        if (type.equals(BOOL)) return (boolean) value;
        fail(34);
        return false;
    }

    public String asString() {
        if (type.equals(STRING)) return (String) value;
        if (type.equals(CHAR)) return String.valueOf((char) (asInt() & 0xFF));
        if (type.equals(INT)) return String.valueOf(asInt());
        if (type.equals(BOOL)) return String.valueOf(asBool());
        if (isNull()) return "null";
        if (isArray()) return arrayToString(array());
        fail(34);
        return "";
    }

    public ArrayValue array() {
        if (!isArray() || isNull()) fail(34);
        return (ArrayValue) value;
    }

    public static String printable(Value value) {
        if (value.type.equals(VOID_TYPE)) fail(34);
        if (value.isNull()) return "null";
        if (value.isArray()) return arrayToString(value.array());
        return value.asString();
    }

    private static String arrayToString(ArrayValue array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.elements.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(printable(array.elements.get(i)));
        }
        sb.append(']');
        return sb.toString();
    }

    public static void fail(int code) {
        System.out.println("Process exits with " + code + ".");
        System.exit(code);
    }
}
