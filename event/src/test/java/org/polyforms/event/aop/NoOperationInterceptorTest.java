package org.polyforms.event.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class NoOperationInterceptorTest {
    @Test
    public void invoke() throws NoSuchMethodException {
        final MethodInvocation invocation = EasyMock.createMock(MethodInvocation.class);

        invocation.getMethod();
        EasyMock.expectLastCall().andReturn(Object.class.getMethod("toString", new Class<?>[0]));
        EasyMock.replay(invocation);

        Assert.assertNull(new NoOperationInterceptor().invoke(invocation));
        EasyMock.verify(invocation);
    }
}
