package org.polyforms.delegation;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationBuilderHolder;

public class DelegationRegisterTest {
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
    public void delegate() {
        delegationBuilder.delegateTo(String.class);
        delegationBuilder.delegateFrom(MockInterface.class);
        EasyMock.expectLastCall().andReturn(mockInterface);
        EasyMock.replay(delegationBuilder);

        new DelegationRegister<MockInterface, String>(){
        }.register(mockInterface);

        EasyMock.verify(delegationBuilder);
    }
    
    public interface MockInterface {
    }
}
