package org.polyforms.delegation.support;

import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.BeanContainer;
import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.support.DelegationExecutorTest.Delegatee.MockException;
import org.polyforms.delegation.support.DelegationExecutorTest.Delegator.DelegateException;
import org.polyforms.parameter.ArgumentProvider;
import org.springframework.core.convert.ConversionService;

public class DelegationExecutorTest {
    private BeanContainer beanContainer;
    private ConversionService conversionService;
    private DelegationExecutor delegationExecutor;
    private Delegation delegation;

    @Before
    public void setUp() {
        beanContainer = EasyMock.createMock(BeanContainer.class);
        conversionService = EasyMock.createMock(ConversionService.class);
        delegationExecutor = new DelegationExecutor(beanContainer, conversionService);
        delegation = EasyMock.createMock(Delegation.class);
    }

    @After
    public void tearDown() {
        EasyMock.verify(beanContainer, conversionService, delegation);
    }

    @Test
    public void domainDelegationExecute() throws Throwable {
        delegation.getDelegateeType();
        EasyMock.expectLastCall().andReturn(String.class).times(2);
        delegation.getDelegateeMethod();
        EasyMock.expectLastCall().andReturn(String.class.getMethod("concat", new Class<?>[] { String.class })).times(2);
        delegation.getDelegateeName();
        EasyMock.expectLastCall().andReturn(null);
        beanContainer.containsBean(String.class);
        EasyMock.expectLastCall().andReturn(false);
        delegation.getArgumentProviders();
        EasyMock.expectLastCall().andReturn(Collections.EMPTY_LIST);
        conversionService.convert("test", String.class);
        EasyMock.expectLastCall().andReturn("test");
        conversionService.convert(4, String.class);
        EasyMock.expectLastCall().andReturn("4");
        delegation.getDelegatorType();
        EasyMock.expectLastCall().andReturn(Delegator.class).times(2);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall()
                .andReturn(Delegator.class.getMethod("concat", new Class<?>[] { String.class, int.class })).times(2);
        conversionService.convert("test4", String.class);
        EasyMock.expectLastCall().andReturn("test4");
        EasyMock.replay(beanContainer, conversionService, delegation);

        Assert.assertEquals("test4", delegationExecutor.execute(delegation, new Object[] { "test", 4 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void domainDelegationExecuteWithZeroParameters() throws Throwable {
        delegation.getDelegateeType();
        EasyMock.expectLastCall().andReturn(String.class);
        delegation.getDelegateeMethod();
        EasyMock.expectLastCall().andReturn(String.class.getMethod("length", new Class<?>[0]));
        delegation.getDelegateeName();
        EasyMock.expectLastCall().andReturn(null);
        beanContainer.containsBean(String.class);
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(beanContainer, conversionService, delegation);

        Assert.assertEquals("4", delegationExecutor.execute(delegation, new Object[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void domainDelegationExecuteWithNullParameter() throws Throwable {
        delegation.getDelegateeType();
        EasyMock.expectLastCall().andReturn(String.class);
        delegation.getDelegateeMethod();
        EasyMock.expectLastCall().andReturn(String.class.getMethod("length", new Class<?>[0]));
        delegation.getDelegateeName();
        EasyMock.expectLastCall().andReturn(null);
        beanContainer.containsBean(String.class);
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(beanContainer, conversionService, delegation);

        Assert.assertEquals("4", delegationExecutor.execute(delegation, new Object[] { null }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void beanDelegationExecuteWithLessParameters() throws Throwable {
        final Delegatee delegatee = EasyMock.createMock(Delegatee.class);

        delegation.getDelegateeType();
        EasyMock.expectLastCall().andReturn(Delegatee.class).times(2);
        delegation.getDelegateeMethod();
        EasyMock.expectLastCall().andReturn(Delegatee.class.getMethod("length", new Class<?>[] { String.class }))
                .times(2);
        delegation.getDelegateeName();
        EasyMock.expectLastCall().andReturn(null);
        beanContainer.containsBean(Delegatee.class);
        EasyMock.expectLastCall().andReturn(true);
        beanContainer.getBean(Delegatee.class);
        EasyMock.expectLastCall().andReturn(delegatee);
        delegation.getArgumentProviders();
        EasyMock.expectLastCall().andReturn(Collections.EMPTY_LIST);
        delegation.getDelegatorType();
        EasyMock.expectLastCall().andReturn(Object.class);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(Object.class.getMethod("toString", new Class<?>[0]));
        EasyMock.replay(beanContainer, conversionService, delegation);

        delegationExecutor.execute(delegation, new Object[0]);
    }

    @Test
    public void beanDelegationExecute() throws Throwable {
        final Delegatee delegatee = EasyMock.createMock(Delegatee.class);

        mockBeanDelegation(delegatee);
        delegation.getDelegatorType();
        EasyMock.expectLastCall().andReturn(Delegator.class);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(Delegator.class.getMethod("length", new Class<?>[] { String.class }));
        delegatee.length("test");
        EasyMock.replay(beanContainer, conversionService, delegation, delegatee);

        delegationExecutor.execute(delegation, new Object[] { "test" });
    }

    private void mockBeanDelegation(final Delegatee delegatee) throws NoSuchMethodException {
        delegation.getDelegateeType();
        EasyMock.expectLastCall().andReturn(Delegatee.class).times(2);
        delegation.getDelegateeMethod();
        EasyMock.expectLastCall().andReturn(Delegatee.class.getMethod("length", new Class<?>[] { String.class }))
                .times(2);
        delegation.getDelegateeName();
        EasyMock.expectLastCall().andReturn(null);
        delegation.getDelegatorType();
        EasyMock.expectLastCall().andReturn(Delegator.class);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(Delegator.class.getMethod("length", new Class<?>[] { String.class }));
        beanContainer.containsBean(Delegatee.class);
        EasyMock.expectLastCall().andReturn(true);
        beanContainer.getBean(Delegatee.class);
        EasyMock.expectLastCall().andReturn(delegatee);
        conversionService.convert(delegatee, Delegatee.class);
        EasyMock.expectLastCall().andReturn(delegatee);
        delegation.getArgumentProviders();
        EasyMock.expectLastCall().andReturn(Collections.EMPTY_LIST);
        conversionService.convert("test", String.class);
        EasyMock.expectLastCall().andReturn("test");
    }

    @Test
    public void beanDelegationExecuteWithName() throws Throwable {
        final Delegatee delegatee = EasyMock.createMock(Delegatee.class);
        final ArgumentProvider argumentProvider = EasyMock.createMock(ArgumentProvider.class);
        final Object[] arguments = new Object[] { "test" };

        delegation.getDelegateeType();
        EasyMock.expectLastCall().andReturn(Delegatee.class);
        delegation.getDelegateeMethod();
        EasyMock.expectLastCall().andReturn(Delegatee.class.getMethod("echo", new Class<?>[] { String.class }));
        delegation.getDelegateeName();
        EasyMock.expectLastCall().andReturn("delegatee");
        beanContainer.getBean("delegatee", Delegatee.class);
        EasyMock.expectLastCall().andReturn(delegatee);
        conversionService.convert(delegatee, Delegatee.class);
        EasyMock.expectLastCall().andReturn(delegatee);
        delegation.getArgumentProviders();
        EasyMock.expectLastCall().andReturn(Collections.singletonList(argumentProvider));
        argumentProvider.get(arguments);
        EasyMock.expectLastCall().andReturn("test");
        conversionService.convert("test", String.class);
        EasyMock.expectLastCall().andReturn("test");
        delegatee.echo("test");
        EasyMock.expectLastCall().andReturn(null);
        delegation.getDelegatorType();
        EasyMock.expectLastCall().andReturn(Delegator.class);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(Delegator.class.getMethod("length", new Class<?>[] { String.class }));
        EasyMock.replay(beanContainer, conversionService, delegation, delegatee, argumentProvider);

        Assert.assertEquals(0, delegationExecutor.execute(delegation, arguments));
    }

    @Test(expected = IllegalStateException.class)
    public void beanDelegationExecuteWithException() throws Throwable {
        final Delegatee delegatee = EasyMock.createMock(Delegatee.class);

        mockBeanDelegation(delegatee);
        delegatee.length("test");
        EasyMock.expectLastCall().andThrow(new IllegalStateException());
        delegation.getExceptionType(IllegalStateException.class);
        EasyMock.expectLastCall().andReturn(null);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(Delegator.class.getMethod("length", new Class<?>[] { String.class }));
        EasyMock.replay(beanContainer, conversionService, delegation, delegatee);

        delegationExecutor.execute(delegation, new Object[] { "test" });
    }

    @Test(expected = DelegateException.class)
    public void beanDelegationExecuteWithNamedException() throws Throwable {
        final Delegatee delegatee = EasyMock.createMock(Delegatee.class);
        final org.polyforms.delegation.support.DelegationExecutorTest.Delegatee.DelegateException delegatorException = new org.polyforms.delegation.support.DelegationExecutorTest.Delegatee.DelegateException();

        mockBeanDelegation(delegatee);
        delegatee.length("test");
        EasyMock.expectLastCall().andThrow(delegatorException);
        delegation
                .getExceptionType(org.polyforms.delegation.support.DelegationExecutorTest.Delegatee.DelegateException.class);
        EasyMock.expectLastCall().andReturn(null);
        delegation.getDelegatorMethod();
        EasyMock.expectLastCall().andReturn(Delegator.class.getMethod("length", new Class<?>[] { String.class }));
        conversionService.convert(delegatorException, DelegateException.class);
        EasyMock.expectLastCall().andReturn(new DelegateException());
        EasyMock.replay(beanContainer, conversionService, delegation, delegatee);

        delegationExecutor.execute(delegation, new Object[] { "test" });
    }

    @Test(expected = DelegateException.class)
    public void beanDelegationExecuteWithMappedException() throws Throwable {
        final Delegatee delegatee = EasyMock.createMock(Delegatee.class);
        final MockException mockException = new MockException();

        mockBeanDelegation(delegatee);
        delegatee.length("test");
        EasyMock.expectLastCall().andThrow(mockException);
        delegation.getExceptionType(MockException.class);
        EasyMock.expectLastCall().andReturn(DelegateException.class);
        conversionService.convert(mockException, DelegateException.class);
        EasyMock.expectLastCall().andReturn(new DelegateException());
        EasyMock.replay(beanContainer, conversionService, delegation, delegatee);

        delegationExecutor.execute(delegation, new Object[] { "test" });
    }

    public interface Delegator {
        int length(String string) throws DelegateException;

        String concat(String string, int number);

        @SuppressWarnings("serial")
        class DelegateException extends Exception {
        }
    }

    public interface Delegatee {
        void length(String string) throws MockException, DelegateException;

        String echo(String string);

        @SuppressWarnings("serial")
        class DelegateException extends Exception {
        }

        @SuppressWarnings("serial")
        class MockException extends Exception {
        }
    }
}
