package org.polyforms.delegation.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.Delegation;

public class GenericDelegationServiceTest {
    private Method method;
    private DelegationExecutor delegationExecutor;
    private DelegationResolver delegationResolver;
    private DelegationService delegationService;

    @Before
    public void setUp() throws NoSuchMethodException {
        method = String.class.getMethod("toString", new Class<?>[0]);
        delegationExecutor = EasyMock.createMock(DelegationExecutor.class);
        delegationResolver = EasyMock.createMock(DelegationResolver.class);
        delegationService = new GenericDelegationService(delegationExecutor, delegationResolver);
    }

    @Test
    public void supports() {
        delegationResolver.supports(new Delegator(String.class, method));
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(delegationResolver);

        Assert.assertTrue(delegationService.supports(String.class, method));
        // Just for testing cache
        Assert.assertTrue(delegationService.supports(String.class, method));
        EasyMock.verify(delegationResolver);
    }

    @Test
    public void notSupports() {
        delegationResolver.supports(new Delegator(String.class, method));
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(delegationResolver);

        Assert.assertFalse(delegationService.supports(String.class, method));
        EasyMock.verify(delegationResolver);
    }

    @Test
    public void notSupportsNullType() {
        Assert.assertFalse(delegationService.supports(null, method));
    }

    @Test
    public void notSupportsNullMethod() {
        Assert.assertFalse(delegationService.supports(String.class, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void delegateNullType() throws Throwable {
        delegationService.delegate(null, method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delegateNullMethod() throws Throwable {
        delegationService.delegate(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delegateUnsupportedMethod() throws Throwable {
        delegationResolver.supports(new Delegator(String.class, method));
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(delegationResolver);

        delegationService.delegate(String.class, method);
        EasyMock.verify(delegationResolver);
    }

    @Test
    public void delegate() throws Throwable {
        final Object returnValue = new Object();
        final Object[] arguments = new Object[0];
        final Delegation delegation = EasyMock.createMock(Delegation.class);

        final Delegator delegator = new Delegator(String.class, method);
        delegationResolver.supports(delegator);
        EasyMock.expectLastCall().andReturn(true);
        delegationResolver.get(delegator);
        EasyMock.expectLastCall().andReturn(delegation);
        delegationExecutor.execute(delegation, arguments);
        EasyMock.expectLastCall().andReturn(returnValue);
        EasyMock.replay(delegationResolver, delegationExecutor);

        Assert.assertSame(returnValue, delegationService.delegate(String.class, method, arguments));
        EasyMock.verify(delegationResolver, delegationExecutor);
    }
}
