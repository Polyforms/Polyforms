package org.polyforms.delegation.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ClassUtils;

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
        PRIVITIVE_MAP.put(Boolean.class, Boolean.FALSE);
        PRIVITIVE_MAP.put(Character.class, primitiveChar);
        PRIVITIVE_MAP.put(Byte.class, primitiveByte);
        PRIVITIVE_MAP.put(Short.class, primitiveShort);
        PRIVITIVE_MAP.put(Integer.class, primitiveInt);
        PRIVITIVE_MAP.put(Long.class, primitiveLong);
        PRIVITIVE_MAP.put(Float.class, primitiveFloat);
        PRIVITIVE_MAP.put(Double.class, primitiveDouble);
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
        return (T) PRIVITIVE_MAP.get(ClassUtils.resolvePrimitiveIfNecessary(type));
    }
}
