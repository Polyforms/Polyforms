package org.polyforms.util;

import org.junit.Assert;
import org.junit.Test;

public class ClassUtilsTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new ClassUtils();
    }

    @Test
    public void primitiveOfBoolean() {
        Assert.assertSame(boolean.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Boolean.class));
    }

    @Test
    public void primitiveOfChar() {
        Assert.assertSame(char.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Character.class));
    }

    @Test
    public void primitiveOfByte() {
        Assert.assertSame(byte.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Byte.class));
    }

    @Test
    public void primitiveOfShort() {
        Assert.assertSame(short.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Short.class));
    }

    @Test
    public void defaultOfInt() {
        Assert.assertSame(int.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Integer.class));
    }

    @Test
    public void primitiveOfLong() {
        Assert.assertSame(long.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Long.class));
    }

    @Test
    public void primitiveOfFloat() {
        Assert.assertSame(float.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Float.class));
    }

    @Test
    public void primitiveOfDouble() {
        Assert.assertSame(double.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Double.class));
    }

    @Test
    public void primitiveOfObject() {
        Assert.assertSame(Object.class, ClassUtils.resolvePrimitiveWrapperIfNecessary(Object.class));
    }
}
