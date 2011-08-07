package org.polyforms.delegation.util;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

public class MethodUtilsTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new MethodUtils();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGetMethodsWithNullClasses() throws Exception {
        MethodUtils.findMostSpecificMethod(null, "echo", String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGetMethodsWithNullMethodName() throws Exception {
        MethodUtils.findMostSpecificMethod(MockInterface.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGetMethodsWithEmptyMethodName() throws Exception {
        MethodUtils.findMostSpecificMethod(MockInterface.class, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGetMethodsWithBlankMethodName() throws Exception {
        MethodUtils.findMostSpecificMethod(MockInterface.class, "  ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGetMethodsWithNullParameterTypes() throws Exception {
        MethodUtils.findMostSpecificMethod(MockInterface.class, "echo");
    }

    @Test
    public void getOverridedMethod() throws Exception {
        final Method method = MockSubClass.class.getMethod("echo", String.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockSubClass.class, "echo", String.class));
    }

    @Test
    public void getUnoverridedMethod() throws Exception {
        final Method method = MockClass.class.getMethod("echo", Integer.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockSubClass.class, "echo", Integer.class));
    }

    @Test
    public void getGenericMethod() throws Exception {
        final Method method = MockClass.class.getMethod("genericEcho", Double.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockClass.class, "genericEcho", Double.class));
    }

    @Test
    public void getMethodFromInterface() throws Exception {
        final Method method = MockInterface.class.getMethod("echo", String.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockInterface.class, "echo", String.class));
    }

    @Test
    public void getMethodByName() throws Exception {
        final Method method = MockClass.class.getMethod("genericEcho", Double.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockClass.class, "genericEcho"));
    }

    @Test
    public void getMethodByNameFromSuper() throws Exception {
        final Method method = MockClass.class.getMethod("superEcho", String.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockSubClass.class, "superEcho"));
    }

    @Test
    public void getMethodByNameFromInterface() throws Exception {
        final Method method = MockInterface.class.getMethod("interfaceEcho", String.class);
        Assert.assertEquals(method, MethodUtils.findMostSpecificMethod(MockSubClass.class, "interfaceEcho"));
    }

    @Test
    public void getInexistentMethod() throws Exception {
        Assert.assertNull(MethodUtils.findMostSpecificMethod(MockClass.class, "notExist"));
    }

    @Test
    public void getUnmatchedMethod() throws Exception {
        Assert.assertNull(MethodUtils.findMostSpecificMethod(MockClass.class, "echo", String.class, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMultipleMethodsFromManyCandidatesByName() throws Exception {
        MethodUtils.findMostSpecificMethod(MockInterface.class, "echo");
    }

    private static interface GenericInterface<T> {
        T genericEcho(T object);
    }

    private static interface MockInterface {
        Integer echo(Integer integer);

        String echo(String string);

        String interfaceEcho(String string);
    }

    private static abstract class MockClass implements GenericInterface<Double> {
        public abstract Double genericEcho(final Double number);

        public abstract Integer echo(Integer integer);

        public abstract String superEcho(String string);

        protected abstract String genericEcho(String string, Integer integer);
    }

    private static abstract class MockSubClass extends MockClass implements MockInterface {
        public abstract String echo(final String string);
    }
}
