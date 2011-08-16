package org.polyforms.di.spring;

import org.junit.Test;
import org.polyforms.di.spring.AbstractMethodOverrideProcessor.UnsupportedMethodReplacer;

public class UnsupportedMethodReplacerTest {
    @Test(expected = UnsupportedOperationException.class)
    public void reimplement() throws NoSuchMethodException {
        new UnsupportedMethodReplacer().reimplement(null, String.class.getMethod("toString", new Class<?>[0]), null);
    }
}
