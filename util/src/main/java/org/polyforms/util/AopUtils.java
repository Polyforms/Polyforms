package org.polyforms.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Proxy;

/**
 * Utility for AOP support.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class AopUtils {
    private static final Deproxyer[] DEPROXYERS = new Deproxyer[] { new NullDeproxyer(), new ProxyDeproxyer(),
            new EnhancerDeproxyer() };

    protected AopUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determine the target class or interfaces of the given class which might be an AOP proxy.
     * 
     * @param clazz of AOP proxy
     * @return target class if proxying by enhancer, or interfaces if proxying by proxy
     */
    public static Class<?>[] deproxy(final Class<?> clazz) {
        for (final Deproxyer deproxyer : DEPROXYERS) {
            if (deproxyer.supports(clazz)) {
                return deproxyer.deproxy(clazz);
            }
        }

        return new Class<?>[] { clazz };
    }

    private abstract static class Deproxyer {
        protected abstract boolean supports(Class<?> clazz);

        protected abstract Class<?>[] deproxy(Class<?> clazz);
    }

    private static class NullDeproxyer extends Deproxyer {
        private static final Class<?>[] EMPTY_CLASS = new Class<?>[0];

        @Override
        protected boolean supports(final Class<?> clazz) {
            return clazz == null || clazz.getSuperclass() == null;
        }

        @Override
        protected Class<?>[] deproxy(final Class<?> clazz) {
            return EMPTY_CLASS.clone();
        }
    }

    private static class ProxyDeproxyer extends Deproxyer {
        @Override
        protected boolean supports(final Class<?> clazz) {
            return java.lang.reflect.Proxy.isProxyClass(clazz) || Proxy.isProxyClass(clazz);
        }

        @Override
        protected Class<?>[] deproxy(final Class<?> clazz) {
            return clazz.getInterfaces();
        }
    }

    private static class EnhancerDeproxyer extends Deproxyer {
        @Override
        protected boolean supports(final Class<?> clazz) {
            return Enhancer.isEnhanced(clazz);
        }

        @Override
        protected Class<?>[] deproxy(final Class<?> clazz) {
            final Class<?>[] interfaces = clazz.getInterfaces();
            final Class<?>[] result = ArrayUtils.copyOf(interfaces, interfaces.length + 1);
            result[interfaces.length] = clazz.getSuperclass();
            return result;
        }
    }
}
