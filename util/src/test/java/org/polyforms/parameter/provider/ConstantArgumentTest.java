package org.polyforms.parameter.provider;

import junit.framework.Assert;

import org.junit.Test;

public class ConstantArgumentTest {
    @Test
    public void constantProvider() {
        final ArgumentProvider constantProvider = new ConstantArgument("test");
        Assert.assertEquals("test", constantProvider.get(new Object[0]));
    }

    @Test
    public void nullProvider() {
        final ArgumentProvider constantProvider = new ConstantArgument(null);
        Assert.assertNull(constantProvider.get(new Object[0]));
    }

    @Test
    public void validate() throws NoSuchMethodException {
        final ArgumentProvider constantProvider = new ConstantArgument("test");
        constantProvider.validate(Object.class.getMethod("toString", new Class<?>[0]));
    }
}
