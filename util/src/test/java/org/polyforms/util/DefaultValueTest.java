package org.polyforms.util;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.util.DefaultValue;

public final class DefaultValueTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new DefaultValue();
    }

    @Test
    public void defaultOfObject() {
        Assert.assertNull(DefaultValue.get(Object.class));
    }

    @Test
    public void defaultOfBoolean() {
        Assert.assertFalse(DefaultValue.get(boolean.class));
    }

    @Test
    public void defaultOfChar() {
        Assert.assertEquals('\u0000', DefaultValue.get(char.class).charValue());
    }

    @Test
    public void defaultOfByte() {
        Assert.assertEquals(0, DefaultValue.get(byte.class).byteValue());
    }

    @Test
    public void defaultOfShort() {
        Assert.assertEquals(0, DefaultValue.get(short.class).shortValue());
    }

    @Test
    public void defaultOfInt() {
        Assert.assertEquals(0, DefaultValue.get(int.class).intValue());
    }

    @Test
    public void defaultOfLong() {
        Assert.assertEquals(0, DefaultValue.get(long.class).longValue());
    }

    @Test
    public void defaultOfFloat() {
        Assert.assertEquals(0F, DefaultValue.get(float.class).floatValue(), 0);
    }

    @Test
    public void defaultOfDouble() {
        Assert.assertEquals(0D, DefaultValue.get(double.class).doubleValue(), 0);
    }

    @Test
    public void defaultOfVoid() {
        Assert.assertNull(DefaultValue.get(void.class));
    }

    @Test
    public void defaultOfNull() {
        Assert.assertNull(DefaultValue.get(null));
    }
}
