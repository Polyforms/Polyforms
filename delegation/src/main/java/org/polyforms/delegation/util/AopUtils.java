package org.polyforms.delegation.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Proxy;

public class AopUtils {
    private static final Class<?>[] EMPTY_CLASS = new Class<?>[0];

    public static Class<?>[] deproxy(final Class<?> clazz) {
        if (clazz == Object.class) {
            return EMPTY_CLASS;
        }

        if (isProxy(clazz)) {
            final Class<?> superClazz = clazz.getSuperclass();
            if (superClazz == Object.class) {
                return clazz.getInterfaces();
            } else {
                return new Class<?>[] { superClazz };
            }
        }

        return new Class<?>[] { clazz };
    }

    private static boolean isProxy(final Class<?> clazz) {
        return Proxy.isProxyClass(clazz) || Enhancer.isEnhanced(clazz) || java.lang.reflect.Proxy.isProxyClass(clazz);
    }
}
