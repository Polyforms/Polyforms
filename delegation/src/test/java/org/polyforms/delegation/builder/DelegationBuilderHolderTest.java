package org.polyforms.delegation.builder;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

public class DelegationBuilderHolderTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new DelegationBuilderHolder();
    }

    @Test
    public void get() {
        Assert.assertNull(DelegationBuilderHolder.get());

        final DelegationBuilder delegationBuilder = EasyMock.createMock(DelegationBuilder.class);
        DelegationBuilderHolder.set(delegationBuilder);
        Assert.assertSame(delegationBuilder, DelegationBuilderHolder.get());

        DelegationBuilderHolder.remove();
        Assert.assertNull(DelegationBuilderHolder.get());
    }
}
