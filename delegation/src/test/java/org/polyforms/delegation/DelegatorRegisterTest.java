package org.polyforms.delegation;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationBuilderHolder;

public class DelegatorRegisterTest {
    private MockInterface mockInterface;
    private DelegationBuilder delegationBuilder;

    @Before
    public void setUp() {
        mockInterface = EasyMock.createMock(MockInterface.class);
        delegationBuilder = EasyMock.createMock(DelegationBuilder.class);
        DelegationBuilderHolder.set(delegationBuilder);
    }

    @After
    public void tearDown() {
        DelegationBuilderHolder.remove();
    }

    @Test
    public void delegateWithParameter() {
        final Object delegatee = new Object();

        delegationBuilder.delegate();
        EasyMock.expectLastCall().andReturn(delegatee);
        EasyMock.replay(delegationBuilder);

        new DelegatorRegister<MockInterface>() {
            @Override
            public void register(final MockInterface source) {
                Assert.assertSame(delegatee, delegate(null));
            }
        }.register(mockInterface);
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void with() {
        final DomainObject domainObject = new DomainObject();
        delegationBuilder.withName("domain");
        delegationBuilder.delegateTo(DomainObject.class);
        delegationBuilder.delegateFrom(MockInterface.class);
        EasyMock.expectLastCall().andReturn(mockInterface);
        delegationBuilder.delegate();
        EasyMock.expectLastCall().andReturn(domainObject);
        delegationBuilder.delegateTo(String.class);
        EasyMock.replay(delegationBuilder);

        new DelegatorRegister<MockInterface>() {
            @Override
            public void register(final MockInterface source) {
                with(new DelegateeRegister<DomainObject>("domain") {
                    @Override
                    public void register(final MockInterface source) {
                        Assert.assertSame(domainObject, delegate(null));
                    }
                });

                // Just for testing cache
                with(new DelegateeRegister<String>() {
                });
            }
        }.register(mockInterface);

        EasyMock.verify(delegationBuilder);
    }

    public interface MockInterface {
    }

    public class DomainObject {
    }
}
