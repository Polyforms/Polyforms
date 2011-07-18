package org.polyforms.di.spring.util;

import org.junit.Test;

public class BeanFactoryVisitorTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new BeanFactoryVisitor();
    }
}
