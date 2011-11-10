package org.polyforms.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility for Class support.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class ClassUtils {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new HashMap<Class<?>, Class<?>>(8);

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
    }

    protected ClassUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Resolve the given class if it is a primitive wrapper class, returning the corresponding primitive type instead.
     * 
     * @param clazz the class to check
     * @return the original class, or a primitive for the original primitive wrapper type
     */
    public static Class<?> resolvePrimitiveWrapperIfNecessary(final Class<?> clazz) {
        if (PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz)) {
            return PRIMITIVE_WRAPPER_TYPE_MAP.get(clazz);
        }

        return clazz;
    }
}
