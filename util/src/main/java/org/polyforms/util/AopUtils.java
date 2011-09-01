package org.polyforms.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Proxy;

public final class AopUtils {
    private static final Deproxyer[] DEPROXYERS = new Deproxyer[] { new NullDeproxyer(), new ProxyDeproxyer(),
            new EnhancerDeproxyer() };

    protected AopUtils() {
        throw new UnsupportedOperationException();
    }

    public static Class<?>[] deproxy(final Class<?> clazz) {
        for (final Deproxyer deproxyer : DEPROXYERS) {
            if (deproxyer.supports(clazz)) {
                return deproxyer.deproxy(clazz);
            }
        }

        return new Class<?>[] { clazz };
    }

    private interface Deproxyer {
        boolean supports(Class<?> clazz);

        Class<?>[] deproxy(Class<?> clazz);
    }

    private static class NullDeproxyer implements Deproxyer {
        private static final Class<?>[] EMPTY_CLASS = new Class<?>[0];

        public boolean supports(final Class<?> clazz) {
            return clazz == null || clazz.getSuperclass() == null;
        }

        public Class<?>[] deproxy(final Class<?> clazz) {
            return EMPTY_CLASS.clone();
        }
    }

    private static class ProxyDeproxyer implements Deproxyer {
        public boolean supports(final Class<?> clazz) {
            return java.lang.reflect.Proxy.isProxyClass(clazz) || Proxy.isProxyClass(clazz);
        }

        public Class<?>[] deproxy(final Class<?> clazz) {
            return clazz.getInterfaces();
        }
    }

    private static class EnhancerDeproxyer implements Deproxyer {
        public boolean supports(final Class<?> clazz) {
            return Enhancer.isEnhanced(clazz);
        }

        public Class<?>[] deproxy(final Class<?> clazz) {
            final Class<?>[] interfaces = clazz.getInterfaces();
            final Class<?>[] result = new Class<?>[interfaces.length + 1];
            System.arraycopy(interfaces, 0, result, 0, interfaces.length);
            result[interfaces.length] = clazz.getSuperclass();
            return result;
        }
    }
}
