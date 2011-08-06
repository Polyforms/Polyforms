package org.polyforms.event.aop;

import org.junit.Assert;
import org.junit.Test;

public class NoOperationInterceptorTest {
    @Test
    public void invoke() {
        Assert.assertSame(Void.TYPE, new NoOperationInterceptor().invoke(null));
    }
}
