package org.polyforms.delegation.support;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.Delegation;

public class SimpleDelegationRegistryTest {
    private SimpleDelegationRegistry delegationRegistry;
    private Delegation delegation;

    @Before
    public void setUp() throws NoSuchMethodException {
        delegationRegistry = new SimpleDelegationRegistry();
        delegation = EasyMock.createMock(Delegation.class);

        delegation.getDelegatorType();
        EasyMock.expectLastCall().andReturn(String.class);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(String.class.getMethod("toString", new Class<?>[0]));
        EasyMock.replay(delegation);

        delegationRegistry.register(delegation);
        EasyMock.verify(delegation);
    }

    @Test
    public void supports() throws NoSuchMethodException {
        Assert.assertTrue(delegationRegistry.contains(String.class, String.class.getMethod("toString", new Class<?>[0])));
    }

    @Test
    public void notSupports() throws NoSuchMethodException {
        Assert.assertFalse(delegationRegistry.contains(String.class,
                String.class.getMethod("hashCode", new Class<?>[0])));
    }

    @Test
    public void get() throws NoSuchMethodException {
        Assert.assertSame(delegation, delegationRegistry.get(new Delegator(String.class, String.class.getMethod(
                "toString", new Class<?>[0]))));
    }
}
