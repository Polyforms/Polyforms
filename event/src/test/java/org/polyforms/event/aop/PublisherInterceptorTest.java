package org.polyforms.event.aop;

import junit.framework.Assert;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.event.Publisher;
import org.polyforms.event.Publisher.When;
import org.polyforms.event.Publishers;
import org.polyforms.event.bus.EventBus;
import org.polyforms.event.bus.support.MethodInvocationEvent;

public class PublisherInterceptorTest {
    private PublisherInterceptor publishInterceptor;
    private EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = EasyMock.createMock(EventBus.class);
        publishInterceptor = new PublisherInterceptor(eventBus);
    }

    @Test
    public void publisher() throws Throwable {
        final Object[] arguments = new Object[0];
        final Object returnValue = new Object();
        final MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
        methodInvocation.getThis();
        EasyMock.expectLastCall().andReturn(this);
        methodInvocation.getMethod();
        EasyMock.expectLastCall().andReturn(this.getClass().getMethod("publisherMethod", new Class<?>[0]));
        methodInvocation.getArguments();
        EasyMock.expectLastCall().andReturn(arguments);
        eventBus.publish(EasyMock.isA(MethodInvocationEvent.class));
        methodInvocation.proceed();
        EasyMock.expectLastCall().andReturn(returnValue);
        EasyMock.replay(methodInvocation, eventBus);

        Assert.assertSame(returnValue, publishInterceptor.invoke(methodInvocation));
        EasyMock.verify(eventBus);
    }

    @Test
    public void publishers() throws Throwable {
        final Object[] arguments = new Object[0];
        final MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
        methodInvocation.getThis();
        EasyMock.expectLastCall().andReturn(this);
        methodInvocation.getMethod();
        EasyMock.expectLastCall().andReturn(this.getClass().getMethod("publishersMethod", new Class<?>[0]));
        methodInvocation.getArguments();
        EasyMock.expectLastCall().andReturn(arguments);
        eventBus.publish(EasyMock.isA(MethodInvocationEvent.class));
        methodInvocation.proceed();
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(methodInvocation, eventBus);

        publishInterceptor.invoke(methodInvocation);
        EasyMock.verify(eventBus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceWithNull() {
        new PublisherInterceptor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeWithNull() throws Throwable {
        publishInterceptor.invoke(null);
    }

    @Publisher(value = "publisher", when = When.BEFORE)
    public Object publisherMethod() {
        return new Object();
    }

    @Publishers(@Publisher("publisher"))
    public void publishersMethod() {
    }
}
