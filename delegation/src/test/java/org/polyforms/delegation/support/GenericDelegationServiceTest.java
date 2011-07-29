package org.polyforms.delegation.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.DelegationRegistry.Delegation;

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
        delegationRegistry.contains(method);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(delegationRegistry);

        Assert.assertTrue(delegationService.canDelegate(method));
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void cannotDelegate() {
        delegationRegistry.contains(method);
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(delegationRegistry);

        Assert.assertFalse(delegationService.canDelegate(method));
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void cannotDelegateNull() {
        Assert.assertFalse(delegationService.canDelegate(null));
    }

    @Test
    public void convert() throws Throwable {
        final Object returnValue = new Object();
        final Object[] arguments = new Object[0];
        final Delegation delegation = new Delegation(method, method);

        delegationRegistry.get(method);
        EasyMock.expectLastCall().andReturn(delegation);
        delegationExecutor.execute(delegation, String.class, arguments);
        EasyMock.expectLastCall().andReturn(returnValue);
        EasyMock.replay(delegationRegistry, delegationExecutor);

        Assert.assertSame(returnValue, delegationService.delegate("Mock String", method, arguments));
        EasyMock.verify(delegationRegistry, delegationExecutor);
    }
}
