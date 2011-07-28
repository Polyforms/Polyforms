package org.polyforms.delegation.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.polyforms.delegation.DelegationService;

public class DelegationInterceptorTest {
    @Test
    public void invoke() throws Throwable {
        final Object target = new Object();
        final Method method = String.class.getMethod("toString", new Class<?>[0]);
        final Object[] arguments = new Object[0];

        final DelegationService delegationService = EasyMock.createMock(DelegationService.class);
        final MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
        methodInvocation.getThis();
        EasyMock.expectLastCall().andReturn(target);
        methodInvocation.getMethod();
        EasyMock.expectLastCall().andReturn(method);
        methodInvocation.getArguments();
        EasyMock.expectLastCall().andReturn(arguments);
        delegationService.delegate(target, method, arguments);
        EasyMock.expectLastCall().andReturn(target);
        EasyMock.replay(delegationService, methodInvocation);

        final MethodInterceptor interceptor = new DelegationInterceptor(delegationService);
        Assert.assertSame(target, interceptor.invoke(methodInvocation));
        EasyMock.verify(delegationService, methodInvocation);
    }
}
