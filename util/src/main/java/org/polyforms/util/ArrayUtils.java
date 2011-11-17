package org.polyforms.util;

import java.lang.reflect.Array;

import org.springframework.util.Assert;

/**
 * Utility for Array support.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class ArrayUtils {
    protected ArrayUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Clone an array.
     */
    public static <T> T[] clone(final T[] origin) {
        return copyOf(origin, origin.length);
    }

    /**
     * Copy an array with specified size.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(final T[] origin, final int size) {
        Assert.notNull(origin);
        Assert.isTrue(size >= 0, "Parameter size must not less than zero.");
        final T[] copied = (T[]) Array.newInstance(origin.getClass().getComponentType(), size);
        System.arraycopy(origin, 0, copied, 0, Math.min(origin.length, size));
        return copied;
    }
}
