package org.polyforms.util;

import java.util.HashMap;
import java.util.Map;

public final class ClassUtils {
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
    }

    protected ClassUtils() {
        throw new UnsupportedOperationException();
    }

    public static Class<?> resolvePrimitiveWrapperIfNecessary(final Class<?> clazz) {
        if (primitiveWrapperTypeMap.containsKey(clazz)) {
            return primitiveWrapperTypeMap.get(clazz);
        }

        return clazz;
    }
}
