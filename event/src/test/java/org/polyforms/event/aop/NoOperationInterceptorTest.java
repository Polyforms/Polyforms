package org.polyforms.event.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class NoOperationInterceptorTest {
    private final NoOperationInterceptor noOperationInterceptor = new NoOperationInterceptor();

    @Test
    public void invoke() throws NoSuchMethodException {
        final MethodInvocation invocation = EasyMock.createMock(MethodInvocation.class);

        invocation.getMethod();
        EasyMock.expectLastCall().andReturn(Object.class.getMethod("toString", new Class<?>[0]));
        EasyMock.replay(invocation);

        Assert.assertNull(noOperationInterceptor.invoke(invocation));
        EasyMock.verify(invocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeWithNull() {
        noOperationInterceptor.invoke(null);
    }
}
