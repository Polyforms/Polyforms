package org.polyforms.parameter.provider;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.parameter.ArgumentProvider;

public class ArgumentAtTest {
    @Test
    public void get() {
        final ArgumentProvider provider = new ArgumentAt(1);
        Assert.assertEquals("test", provider.get(new Object[] { 0, "test" }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativePosition() {
        new ArgumentAt(-1);
    }

    @Test
    public void validate() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentAt(0);
        provider.validate(String.class.getMethod("concat", new Class<?>[] { String.class }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void positionOutOfBound() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentAt(1);
        provider.validate(String.class.getMethod("concat", new Class<?>[] { String.class }));
    }
}
