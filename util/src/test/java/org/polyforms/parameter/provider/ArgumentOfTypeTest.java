package org.polyforms.parameter.provider;

import junit.framework.Assert;

import org.junit.Test;

public class ArgumentOfTypeTest {
    @Test
    public void get() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentOfType(String.class);
        provider.validate(String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class }));
        Assert.assertEquals("test", provider.get(new Object[] { "test", 0 }));
    }

    @Test
    public void nullArgument() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentOfType(String.class);
        provider.validate(String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class }));
        Assert.assertNull(provider.get(new Object[] { null, 0 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBeforeValidation() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentOfType(String.class);
        provider.get(new Object[] { "test", 0 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullType() {
        new ArgumentOfType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noParameterOfType() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentOfType(String.class);
        provider.validate(String.class.getMethod("charAt", new Class<?>[] { int.class }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void moreThanOneParameterOfType() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentOfType(String.class);
        provider.validate(String.class.getMethod("replaceAll", new Class<?>[] { String.class, String.class }));
    }
}
