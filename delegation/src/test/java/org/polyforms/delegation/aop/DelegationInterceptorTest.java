package org.polyforms.delegation.aop;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Test;
import org.polyforms.delegation.DelegationService;

public class DelegationInterceptorTest {
    @Test
    public void invoke() throws Throwable {
        Object target = new Object();
        Method method = String.class.getMethod("toString", new Class<?>[0]);
        Object[] arguments = new Object[0];

        DelegationService delegationService = EasyMock.createMock(DelegationService.class);
        MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
        methodInvocation.getThis();
        EasyMock.expectLastCall().andReturn(target);
        methodInvocation.getMethod();
        EasyMock.expectLastCall().andReturn(method);
        methodInvocation.getArguments();
        EasyMock.expectLastCall().andReturn(arguments);
        delegationService.delegate(target, method, arguments);
        EasyMock.expectLastCall().andReturn(target);
        EasyMock.replay(delegationService, methodInvocation);

        MethodInterceptor interceptor = new DelegationInterceptor(delegationService);
        Assert.assertSame(target, interceptor.invoke(methodInvocation));
        EasyMock.verify(delegationService, methodInvocation);
    }
}
