package org.polyforms.parameter.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.parameter.annotation.Named;

public class MethodParametersTest {
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
    }

    public void annotatedMethod(@Deprecated @Named("named") final String string, final int index) {
    }
}
