package org.polyforms.parameter.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.polyforms.parameter.annotation.Named;

public class MethodParametersTest {
    private MethodParameters methodParametersA;
    private MethodParameters methodParametersB;

    @Before
    public void setUp() throws NoSuchMethodException {
        methodParametersA = new MethodParameters(Object.class, Object.class.getMethod("equals",
                new Class<?>[] { Object.class }));
        methodParametersB = new MethodParameters(String.class, String.class.getMethod("indexOf", new Class<?>[] {
                String.class, int.class }));
    }

    @Test
    public void getParameters() throws NoSuchMethodException {
        final Method method = String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class });
        final MethodParameters methodParameters = new MethodParameters(String.class, method);
        final MethodParameter[] parameters = methodParameters.getParameters();
        Assert.assertEquals(2, parameters.length);

        final MethodParameter stringParameter = parameters[0];
        Assert.assertEquals(String.class, stringParameter.getType());
        Assert.assertNull(stringParameter.getName());
        Assert.assertEquals(0, stringParameter.getIndex());

        final MethodParameter intParameter = parameters[1];
        Assert.assertEquals(Integer.class, intParameter.getType());
        Assert.assertNull(intParameter.getName());
        Assert.assertEquals(1, intParameter.getIndex());

        final MethodParameter returnParameter = methodParameters.getReturnParameter();
        Assert.assertEquals(Integer.class, returnParameter.getType());
        Assert.assertEquals("returnValue", returnParameter.getName());
        Assert.assertEquals(2, returnParameter.getIndex());
    }

    @Test
    public void getParametersWithAnnotation() throws NoSuchMethodException {
        final Method method = this.getClass().getMethod("annotatedMethod", new Class<?>[] { String.class, int.class });
        final MethodParameters methodParameters = new MethodParameters(this.getClass(), method);
        methodParameters.applyAnnotation();
        final MethodParameter[] parameters = methodParameters.getParameters();
        Assert.assertEquals(2, parameters.length);

        final MethodParameter stringParameter = parameters[0];
        Assert.assertEquals(String.class, stringParameter.getType());
        Assert.assertEquals("named", stringParameter.getName());
        Assert.assertEquals(0, stringParameter.getIndex());

        final MethodParameter intParameter = parameters[1];
        Assert.assertEquals(Integer.class, intParameter.getType());
        Assert.assertEquals("index", intParameter.getName());
        Assert.assertEquals(1, intParameter.getIndex());

        Assert.assertNull(methodParameters.getReturnParameter());
    }

    @Test
    public void hashcode() {
        Assert.assertTrue(methodParametersA.hashCode() != methodParametersB.hashCode());
    }

    @Test
    public void equalsSame() {
        Assert.assertTrue(methodParametersA.equals(methodParametersA));
    }

    @Test
    public void notEqualsNull() {
        Assert.assertFalse(methodParametersA.equals(null));
    }

    @Test
    public void notEqualsOtherClass() {
        Assert.assertFalse(methodParametersA.equals(new Object()));
    }

    @Test
    public void notEqualsSource() throws NoSuchMethodException {
        Assert.assertFalse(methodParametersA.equals(new MethodParameters(String.class, Object.class.getMethod(
                "hashCode", new Class<?>[0]))));
    }

    @Test
    public void notEqualsMethod() throws NoSuchMethodException {
        Assert.assertFalse(methodParametersA.equals(new MethodParameters(String.class, Object.class.getMethod("equals",
                new Class<?>[] { Object.class }))));
    }

    @Test
    public void equals() throws NoSuchMethodException {
        Assert.assertTrue(methodParametersA.equals(new MethodParameters(Object.class, Object.class.getMethod("equals",
                new Class<?>[] { Object.class }))));
    }

    public void annotatedMethod(@Deprecated @Named("named") final String string, final int index) {
    }
}
