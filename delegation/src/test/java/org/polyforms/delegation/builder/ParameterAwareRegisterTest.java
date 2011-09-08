package org.polyforms.delegation.builder;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.provider.At;
import org.polyforms.delegation.provider.Constant;
import org.polyforms.delegation.provider.TypeOf;

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
        delegationBuilder.parameter(EasyMock.isA(At.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.at(String.class, 0));
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void typeOf() {
        delegationBuilder.parameter(EasyMock.isA(TypeOf.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertEquals(0, parameterAwareRegister.typeOf(int.class, String.class).intValue());
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void constant() {
        delegationBuilder.parameter(EasyMock.isA(Constant.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.constant("test"));
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void constantNull() {
        delegationBuilder.parameter(EasyMock.isA(Constant.class));
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.constant(null));
        EasyMock.verify(delegationBuilder);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void provideBy() {
        final ParameterProvider<String> parameterProvider = EasyMock.createMock(ParameterProvider.class);
        delegationBuilder.parameter(parameterProvider);
        EasyMock.replay(delegationBuilder);

        Assert.assertNull(parameterAwareRegister.provideBy(String.class, parameterProvider));
        EasyMock.verify(delegationBuilder);
    }
}
