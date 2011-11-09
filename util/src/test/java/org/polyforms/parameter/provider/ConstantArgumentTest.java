package org.polyforms.parameter.provider;

import junit.framework.Assert;

import org.junit.Test;

public class ConstantArgumentTest {
    @Test
    public void get() {
        final ArgumentProvider provider = new ConstantArgument("test");
        Assert.assertEquals("test", provider.get(new Object[0]));
    }

    @Test
    public void nullArgument() {
        final ArgumentProvider provider = new ConstantArgument(null);
        Assert.assertNull(provider.get(new Object[0]));
    }

    @Test
    public void validate() throws NoSuchMethodException {
        final ArgumentProvider provider = new ConstantArgument("test");
        provider.validate(Object.class.getMethod("toString", new Class<?>[0]));
    }
}
