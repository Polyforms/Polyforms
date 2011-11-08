package org.polyforms.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for getting default value for primitive types and object types.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class DefaultValue {

    private static final Map<Class<?>, Object> PRIMITIVE_MAP = new HashMap<Class<?>, Object>();
    private static char primitiveChar;
    private static byte primitiveByte;
    private static short primitiveShort;
    private static int primitiveInt;
    private static long primitiveLong;
    private static float primitiveFloat;
    private static double primitiveDouble;

    static {
        PRIMITIVE_MAP.put(boolean.class, Boolean.FALSE);
        PRIMITIVE_MAP.put(char.class, primitiveChar);
        PRIMITIVE_MAP.put(byte.class, primitiveByte);
        PRIMITIVE_MAP.put(short.class, primitiveShort);
        PRIMITIVE_MAP.put(int.class, primitiveInt);
        PRIMITIVE_MAP.put(long.class, primitiveLong);
        PRIMITIVE_MAP.put(float.class, primitiveFloat);
        PRIMITIVE_MAP.put(double.class, primitiveDouble);
    }

    protected DefaultValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return default value of type , which is null for object types and default value of fields in class for primitive
     * types.
     * 
     * @param type of default value
     * @return default value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(final Class<T> type) {
        return (T) PRIMITIVE_MAP.get(type);
    }
}
