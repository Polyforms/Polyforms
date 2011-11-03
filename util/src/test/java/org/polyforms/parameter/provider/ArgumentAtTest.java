package org.polyforms.parameter.provider;

import junit.framework.Assert;

import org.junit.Test;

public class ArgumentAtTest {
    @Test
    public void atProvider() {
        final ArgumentProvider atProvider = new ArgumentAt(1);
        Assert.assertEquals("test", atProvider.get(new Object[] { 0, "test" }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void atProvideWithNegativePosition() {
        new ArgumentAt(-1);
    }

    @Test
    public void validate() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentAt(0);
        atProvider.validate(String.class.getMethod("concat", new Class<?>[] { String.class }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void positionOutOfBound() throws NoSuchMethodException {
        final ArgumentProvider atProvider = new ArgumentAt(1);
        atProvider.validate(String.class.getMethod("concat", new Class<?>[] { String.class }));
    }
}
