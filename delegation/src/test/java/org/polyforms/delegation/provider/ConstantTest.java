package org.polyforms.delegation.provider;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.delegation.builder.ParameterProvider;

public class ConstantTest {
    @Test
    public void constantProvider() {
        final ParameterProvider<String> constantProvider = new Constant<String>("test");
        Assert.assertEquals("test", constantProvider.get(new Object[0]));
    }

    @Test
    public void nullProvider() {
        final ParameterProvider<String> constantProvider = new Constant<String>(null);
        Assert.assertNull(constantProvider.get(new Object[0]));
    }

    @Test
    public void validate() {
        final ParameterProvider<String> constantProvider = new Constant<String>("test");
        constantProvider.validate(new Class<?>[0]);
    }
}
