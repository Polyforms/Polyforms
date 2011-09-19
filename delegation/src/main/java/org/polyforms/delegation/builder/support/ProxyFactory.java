package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.proxy.Proxy;

import org.polyforms.util.DefaultValue;

/**
 * Factory used to create proxy for specified class or interface.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
final class ProxyFactory {
    private static final Class<?>[] EMPTY_INTERFACES = new Class<?>[0];
    private static final CallbackFilter CALLBACK_FILTER = new CallbackFilter() {
        /**
         * {@inheritDoc}
         */
        public int accept(final Method method) {
            return method.isBridge() || method.getName().equals("finalize") && method.getParameterTypes().length == 0 ? 1
                    : 0;
        }
    };
    private final Callback[] callbacks;
    private final InvocationHandler invocationHandler;

    protected ProxyFactory(final MethodVisitor methodVisitor) {
        final MethodInterceptor methodIntercepter = new MethodInterceptor() {
            /**
             * {@inheritDoc}
             */
            public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) {
                return visitMethod(methodVisitor, method);
            }
        };
        callbacks = new Callback[] { methodIntercepter, NoOp.INSTANCE };

        invocationHandler = new InvocationHandler() {
            /**
             * {@inheritDoc}
             */
            public Object invoke(final Object proxy, final Method method, final Object[] args) {
                return visitMethod(methodVisitor, method);
            }
        };
    }

    private Object visitMethod(final MethodVisitor methodVisitor, final Method method) {
        methodVisitor.visit(method);
        return DefaultValue.get(method.getReturnType());
    }

    protected <T> T getProxy(final Class<T> proxyClass) {
        if (proxyClass == null || Modifier.isFinal(proxyClass.getModifiers())) {
            return null;
        }

        return proxyClass.isInterface() ? proxyForInterface(proxyClass) : proxyForClass(proxyClass);
    }

    @SuppressWarnings("unchecked")
    private <T> T proxyForInterface(final Class<T> proxyClass) {
        return (T) Proxy
                .newProxyInstance(proxyClass.getClassLoader(), new Class<?>[] { proxyClass }, invocationHandler);
    }

    @SuppressWarnings("unchecked")
    private <T> T proxyForClass(final Class<T> proxyClass) {
        return (T) Enhancer.create(proxyClass, EMPTY_INTERFACES, CALLBACK_FILTER, callbacks);
    }

    protected abstract static class MethodVisitor {
        protected abstract void visit(Method method);
    }
}
