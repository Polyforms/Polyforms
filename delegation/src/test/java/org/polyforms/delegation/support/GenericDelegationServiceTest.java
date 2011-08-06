package org.polyforms.delegation.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.Delegator;

public class GenericDelegationServiceTest {
    private Method method;
    private DelegationExecutor delegationExecutor;
    private DelegationRegistry delegationRegistry;
    private DelegationService delegationService;

    @Before
    public void setUp() throws NoSuchMethodException {
        method = String.class.getMethod("toString", new Class<?>[0]);
        delegationExecutor = EasyMock.createMock(DelegationExecutor.class);
        delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
        delegationService = new GenericDelegationService(delegationExecutor, delegationRegistry);
    }

    @Test
    public void canDelegate() {
        delegationRegistry.supports(new Delegator(String.class, method));
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(delegationRegistry);

        Assert.assertTrue(delegationService.supports(String.class, method));
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void cannotDelegate() {
        delegationRegistry.supports(new Delegator(String.class, method));
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(delegationRegistry);

        Assert.assertFalse(delegationService.supports(String.class, method));
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void cannotDelegateNull() {
        Assert.assertFalse(delegationService.supports(String.class, null));
    }

    @Test
    public void convert() throws Throwable {
        final Object returnValue = new Object();
        final Object[] arguments = new Object[0];
        final Delegation delegation = EasyMock.createMock(Delegation.class);

        final Delegator delegator = new Delegator(String.class, method);
        delegationRegistry.supports(delegator);
        EasyMock.expectLastCall().andReturn(true);
        delegationRegistry.get(delegator);
        EasyMock.expectLastCall().andReturn(delegation);
        delegationExecutor.execute(delegation, arguments);
        EasyMock.expectLastCall().andReturn(returnValue);
        EasyMock.replay(delegationRegistry, delegationExecutor);

        Assert.assertSame(returnValue, delegationService.delegate(String.class, method, arguments));
        EasyMock.verify(delegationRegistry, delegationExecutor);
    }
}
