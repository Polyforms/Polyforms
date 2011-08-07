package org.polyforms.delegation.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Proxy;

public final class AopUtils {
    private static final Class<?>[] EMPTY_CLASS = new Class<?>[0];

    protected AopUtils() {
        throw new UnsupportedOperationException();
    }

    public static Class<?>[] deproxy(final Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return EMPTY_CLASS;
        }

        if (java.lang.reflect.Proxy.isProxyClass(clazz) || Proxy.isProxyClass(clazz)) {
            return clazz.getInterfaces();
        }

        if (Enhancer.isEnhanced(clazz)) {
            final Class<?>[] interfaces = clazz.getInterfaces();
            final Class<?>[] result = new Class<?>[interfaces.length + 1];
            System.arraycopy(interfaces, 0, result, 0, interfaces.length);
            result[interfaces.length] = clazz.getSuperclass();
            return result;
        }

        return new Class<?>[] { clazz };
    }
}
