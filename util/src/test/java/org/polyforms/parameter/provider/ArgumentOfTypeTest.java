package org.polyforms.parameter.provider;

import junit.framework.Assert;

import org.junit.Test;

public class ArgumentOfTypeTest {
    @Test
    public void atProvider() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentOfType(String.class);
        atProvider.validate(String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class }));
        Assert.assertEquals("test", atProvider.get(new Object[] { "test", 0 }));
    }

    @Test
    public void atProviderWithNull() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentOfType(String.class);
        atProvider.validate(String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class }));
        Assert.assertNull(atProvider.get(new Object[] { null, 0 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void atProvideWithNegativePosition() {
        new ArgumentOfType(null);
    }

    @Test
    public void validate() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentOfType(String.class);
        atProvider.validate(String.class.getMethod("concat", new Class<?>[] { String.class }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noParameterOfType() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentOfType(String.class);
        atProvider.validate(String.class.getMethod("charAt", new Class<?>[] { int.class }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void moreThanOneParameterOfType() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentOfType(String.class);
        atProvider.validate(String.class.getMethod("replaceAll", new Class<?>[] { String.class, String.class }));
    }
}
