package org.polyforms.parameter.provider;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.parameter.ArgumentProvider;

public class ReturnValueTest {
    @Test
    public void get() throws NoSuchMethodException {
        final ArgumentProvider provider = new ReturnValue();
        provider.validate(String.class.getMethod("substring", new Class<?>[] { int.class }));
        Assert.assertEquals("returnValue", provider.get(new Object[] { 0, "returnValue" }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void voidMethod() throws NoSuchMethodException {
        final ArgumentProvider provider = new ReturnValue();
        provider.validate(String.class.getMethod("getChars", new Class<?>[] { int.class, int.class, char[].class,
                int.class }));
    }
}
