package org.polyforms.delegation;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationBuilderHolder;
import org.polyforms.parameter.provider.ArgumentAt;
import org.polyforms.parameter.provider.ArgumentOfType;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.polyforms.parameter.provider.ConstantArgument;

public class ParameterAwareRegisterTest {
    private DelegationBuilder delegationBuilder;
    private ParameterAwareRegister<String> parameterAwareRegister;

    @Before
    public void setUp() {
        delegationBuilder = EasyMock.createMock(DelegationBuilder.class);
        DelegationBuilderHolder.set(delegationBuilder);
        parameterAwareRegister = new ParameterAwareRegister<String>() {
        };
    }

    @After
    public void tearDown() {
        DelegationBuilderHolder.remove();
    }

    @Test
    public void register() {
        parameterAwareRegister.register(null);
    }

    @Test
    public void map() {
        delegationBuilder.map(RuntimeException.class, Exception.class);
        EasyMock.replay(delegationBuilder);

        parameterAwareRegister.map(RuntimeException.class, Exception.class);
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void at() {
        delegationBuilder.parameter(EasyMock.isA(ArgumentAt.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.at(String.class, 0));
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void typeOf() {
        delegationBuilder.parameter(EasyMock.isA(ArgumentOfType.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertEquals(0, parameterAwareRegister.typeOf(int.class, String.class).intValue());
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void constant() {
        delegationBuilder.parameter(EasyMock.isA(ConstantArgument.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.constant("test"));
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void constantNull() {
        delegationBuilder.parameter(EasyMock.isA(ConstantArgument.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.constant(null));
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void provideBy() {
        final ArgumentProvider argumentProvider = EasyMock.createMock(ArgumentProvider.class);
        delegationBuilder.parameter(argumentProvider);
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.provideBy(String.class, argumentProvider));
        EasyMock.verify(delegationBuilder);
    }
}
