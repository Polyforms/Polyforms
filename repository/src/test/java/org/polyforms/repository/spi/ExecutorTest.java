package org.polyforms.repository.spi;

import org.junit.Test;

public class ExecutorTest {
    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedExecutor() throws Exception {
        Executor.UNSUPPORTED.execute(new Object(), Object.class.getMethod("toString", new Class<?>[0]), new Object[0]);
    }
}
