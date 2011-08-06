package org.polyforms.delegation.util;

import org.junit.Assert;
import org.junit.Test;

public final class DefaultValueTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new DefaultValue();
    }

    @Test
    public void returnObject() {
        Assert.assertNull(DefaultValue.get(Object.class));
    }

    @Test
    public void returnBoolean() {
        Assert.assertFalse(DefaultValue.get(boolean.class));
    }

    @Test
    public void returnChar() {
        Assert.assertEquals('\u0000', DefaultValue.get(char.class).charValue());
    }

    @Test
    public void returnByte() {
        Assert.assertEquals(0, DefaultValue.get(byte.class).byteValue());
    }

    @Test
    public void returnShort() {
        Assert.assertEquals(0, DefaultValue.get(short.class).shortValue());
    }

    @Test
    public void returnInt() {
        Assert.assertEquals(0, DefaultValue.get(int.class).intValue());
    }

    @Test
    public void returnLong() {
        Assert.assertEquals(0, DefaultValue.get(long.class).longValue());
    }

    @Test
    public void returnFloat() {
        Assert.assertEquals(0F, DefaultValue.get(float.class).floatValue(), 0);
    }

    @Test
    public void returnDouble() {
        Assert.assertEquals(0D, DefaultValue.get(double.class).doubleValue(), 0);
    }

    @Test
    public void returnNull() {
        Assert.assertNull(DefaultValue.get(void.class));
    }
}
