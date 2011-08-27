package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.proxy.Proxy;

import org.polyforms.delegation.util.DefaultValue;
import org.springframework.util.ClassUtils;

final class Cglib2ProxyFactory implements ProxyFactory {
    private static final Class<?>[] CALLBACK_TYPES = new Class[] { MethodInterceptor.class, NoOp.class };
    private static final CallbackFilter CALLBACK_FILTER = new CallbackFilter() {
        public int accept(final Method method) {
            return method.isBridge() || method.getName().equals("finalize") && method.getParameterTypes().length == 0 ? 1
                    : 0;
        }
    };
    private final Map<Class<?>, Object> proxyCache = new HashMap<Class<?>, Object>();
    private final MethodInterceptor methodIntercepter;
    private final InvocationHandler invocationHandler;

    public Cglib2ProxyFactory(final MethodVisitor methodVisitor) {
        methodIntercepter = new MethodInterceptor() {
            public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) {
                return visitMethod(methodVisitor, method);
            }
        };
        invocationHandler = new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) {
                return visitMethod(methodVisitor, method);
            }
        };
    }

    private Object visitMethod(final MethodVisitor methodVisitor, final Method method) {
        methodVisitor.visit(method);
        return DefaultValue.get(method.getReturnType());
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(final Class<T> proxyClass) {
        if (proxyClass == null || Modifier.isFinal(proxyClass.getModifiers())) {
            return null;
        }
        if (!proxyCache.containsKey(proxyClass)) {
            T proxy;
            if (proxyClass.isInterface()) {
                proxy = proxyForInterface(proxyClass);
            } else {
                proxy = proxyForClass(proxyClass);
            }
            proxyCache.put(proxyClass, proxy);
        }
        return (T) proxyCache.get(proxyClass);
    }

    @SuppressWarnings("unchecked")
    private <T> T proxyForInterface(final Class<T> proxyClass) {
        return (T) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class<?>[] { proxyClass },
                invocationHandler);
    }

    @SuppressWarnings("unchecked")
    private <T> T proxyForClass(final Class<T> proxyClass) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(proxyClass);
        enhancer.setCallbackFilter(CALLBACK_FILTER);
        enhancer.setCallbackTypes(CALLBACK_TYPES);
        enhancer.setCallbacks(new Callback[] { methodIntercepter, NoOp.INSTANCE });
        return (T) enhancer.create();
    }

    interface MethodVisitor {
        void visit(Method method);
    }
}
