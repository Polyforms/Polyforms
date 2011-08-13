package org.polyforms.delegation.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for getting default value for type.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class DefaultValue {
    private static final Map<Class<?>, Object> PRIVITIVE_MAP = new HashMap<Class<?>, Object>();
    private static char primitiveChar;
    private static byte primitiveByte;
    private static short primitiveShort;
    private static int primitiveInt;
    private static long primitiveLong;
    private static float primitiveFloat;
    private static double primitiveDouble;

    static {
        PRIVITIVE_MAP.put(boolean.class, Boolean.FALSE);
        PRIVITIVE_MAP.put(char.class, primitiveChar);
        PRIVITIVE_MAP.put(byte.class, primitiveByte);
        PRIVITIVE_MAP.put(short.class, primitiveShort);
        PRIVITIVE_MAP.put(int.class, primitiveInt);
        PRIVITIVE_MAP.put(long.class, primitiveLong);
        PRIVITIVE_MAP.put(float.class, primitiveFloat);
        PRIVITIVE_MAP.put(double.class, primitiveDouble);
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
        return (T) PRIVITIVE_MAP.get(type);
    }
}
