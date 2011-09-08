package org.polyforms.delegation.provider;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.delegation.builder.ParameterProvider;

public class AtTest {
    @Test
    public void atProvider() {
        final ParameterProvider<String> atProvider = new At<String>(1);
        Assert.assertEquals("test", atProvider.get(new Object[] { 0, "test" }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void atProvideWithNegativePosition() {
        new At<String>(-1);
    }

    @Test
    public void validate() {
        final ParameterProvider<String> atProvider = new At<String>(0);
        atProvider.validate(new Class<?>[] { String.class });
    }

    @Test(expected = IllegalArgumentException.class)
    public void positionOutOfBound() {
        final ParameterProvider<String> atProvider = new At<String>(1);
        atProvider.validate(new Class<?>[] { String.class });
    }
}
