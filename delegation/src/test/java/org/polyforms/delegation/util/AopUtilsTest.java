package org.polyforms.delegation.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.proxy.Proxy;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.ClassUtils;

public class AopUtilsTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new AopUtils();
    }

    @Test
    public void deproxyForNull() {
        Assert.assertEquals(0, AopUtils.deproxy(null).length);
    }

    @Test
    public void deproxyForObject() {
        Assert.assertEquals(0, AopUtils.deproxy(Object.class).length);
    }

    @Test
    public void deproxyNotObject() {
        final Class<?>[] classes = AopUtils.deproxy(String.class);
        Assert.assertTrue(contains(classes, String.class));
    }

    @Test
    public void deproxyForJavaProxy() {
        final Class<?> javaProxyClass = java.lang.reflect.Proxy.getProxyClass(ClassUtils.getDefaultClassLoader(),
                new Class<?>[] { MockInterface.class });
        final Class<?>[] classes = AopUtils.deproxy(javaProxyClass);
        Assert.assertTrue(contains(classes, MockInterface.class));
    }

    @Test
    public void deproxyForCglibProxy() {
        final Class<?> cglibProxyClass = Proxy.getProxyClass(ClassUtils.getDefaultClassLoader(),
                new Class<?>[] { MockInterface.class });
        final Class<?>[] classes = AopUtils.deproxy(cglibProxyClass);
        Assert.assertTrue(contains(classes, MockInterface.class));
    }

    @Test
    public void deproxyForCglibEnhancer() {
        final Class<?> cglibProxyClass = createEnhancer(MockClass.class, new Class<?>[] { MockInterface.class });
        final Class<?>[] classes = AopUtils.deproxy(cglibProxyClass);
        Assert.assertTrue(contains(classes, MockClass.class));
        Assert.assertTrue(contains(classes, MockInterface.class));
    }

    private Class<?> createEnhancer(final Class<?> superClass, final Class<?>[] interfaces) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superClass);
        enhancer.setInterfaces(interfaces);
        enhancer.setCallbackTypes(new Class[] { NoOp.class });
        return enhancer.createClass();
    }

    private boolean contains(final Class<?>[] classes, final Class<?> target) {
        for (final Class<?> clazz : classes) {
            if (clazz == target) {
                return true;
            }
        }

        return false;
    }

    public interface MockInterface {
    }

    public class MockClass {
    }
}
