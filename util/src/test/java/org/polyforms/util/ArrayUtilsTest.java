package org.polyforms.util;

import org.junit.Assert;
import org.junit.Test;

public class ArrayUtilsTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new ArrayUtils();
    }

    @Test
    public void cloneArray() {
        final Integer[] origin = new Integer[] { 1, 2 };
        Assert.assertArrayEquals(origin, ArrayUtils.clone(origin));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullOrigin() {
        ArrayUtils.copyOf(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nagetiveSize() {
        ArrayUtils.copyOf(new Integer[] { 1, 2 }, -2);
    }
}
