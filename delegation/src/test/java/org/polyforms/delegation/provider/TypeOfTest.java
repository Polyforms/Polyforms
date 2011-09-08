package org.polyforms.delegation.provider;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.delegation.builder.ParameterProvider;

public class TypeOfTest {
    @Test
    public void atProvider() {
        final ParameterProvider<String> atProvider = new TypeOf<String>(String.class);
        Assert.assertEquals("test", atProvider.get(new Object[] { 0, "test" }));
    }

    @Test
    public void atProviderWithNull() {
        final ParameterProvider<String> atProvider = new TypeOf<String>(String.class);
        Assert.assertNull(atProvider.get(new Object[] { 0, null }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void atProvideWithNegativePosition() {
        new TypeOf<String>(null);
    }

    @Test
    public void validate() {
        final ParameterProvider<String> atProvider = new TypeOf<String>(String.class);
        atProvider.validate(new Class<?>[] { String.class });
    }

    @Test(expected = IllegalArgumentException.class)
    public void noParameterOfType() {
        final ParameterProvider<String> atProvider = new TypeOf<String>(String.class);
        atProvider.validate(new Class<?>[] { Integer.class });
    }

    @Test(expected = IllegalArgumentException.class)
    public void moreThanOneParameterOfType() {
        final ParameterProvider<String> atProvider = new TypeOf<String>(String.class);
        atProvider.validate(new Class<?>[] { String.class, String.class });
    }
}
