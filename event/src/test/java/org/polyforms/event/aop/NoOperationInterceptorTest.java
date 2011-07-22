package org.polyforms.event.aop;

import junit.framework.Assert;

import org.junit.Test;

public class NoOperationInterceptorTest {
    @Test
    public void invoke() {
        Assert.assertSame(Void.TYPE, new NoOperationInterceptor().invoke(null));
    }
}
