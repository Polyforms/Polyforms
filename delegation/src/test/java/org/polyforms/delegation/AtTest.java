package org.polyforms.delegation;

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
}
