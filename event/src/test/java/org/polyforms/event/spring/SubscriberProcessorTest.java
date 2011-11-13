package org.polyforms.event.spring;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.event.Subscriber;
import org.polyforms.event.bus.Listener;
import org.polyforms.event.bus.ListenerRegistry;
import org.polyforms.event.bus.support.MethodInvocationEvent;
import org.polyforms.event.spring.SubscriberProcessor.SpringBeanMethodInvoker;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.convert.ConversionService;

public class SubscriberProcessorTest {
    private BeanFactory beanFactory;
    private ListenerRegistry listenerRegistry;
    private SubscriberProcessor subscriberProcessor;

    @Before
    public void setUp() {
        listenerRegistry = EasyMock.createMock(ListenerRegistry.class);
        beanFactory = EasyMock.createMock(BeanFactory.class);
        subscriberProcessor = new SubscriberProcessor();
        subscriberProcessor.setBeanFactory(beanFactory);
    }

    @Test
    public void postProcessBeforeInitialization() {
        Object bean = new Object();
        Assert.assertSame(bean, subscriberProcessor.postProcessBeforeInitialization(bean, "beanName"));
    }

    @Test
    public void postProcessAfterInitialization() {
        beanFactory.containsBean("inexistedBeanName");
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.containsBean("beanName");
        EasyMock.expectLastCall().andReturn(true);
        beanFactory.getBean(ListenerRegistry.class);
        EasyMock.expectLastCall().andReturn(listenerRegistry);
        listenerRegistry
                .register(EasyMock.eq("event"), EasyMock.isA(SpringBeanMethodInvoker.class), EasyMock.eq(false));
        EasyMock.replay(beanFactory, listenerRegistry);

        subscriberProcessor.postProcessAfterInitialization(this, "inexistedBeanName");
        subscriberProcessor.postProcessAfterInitialization(this, "beanName");
        EasyMock.verify(beanFactory, listenerRegistry);
    }

    @Test
    public void postProcessBeforeDestruction() {
        beanFactory.containsBean("beanName");
        EasyMock.expectLastCall().andReturn(true);
        beanFactory.getBean(ListenerRegistry.class);
        EasyMock.expectLastCall().andReturn(listenerRegistry);
        listenerRegistry.unregister(EasyMock.eq("event"), EasyMock.isA(SpringBeanMethodInvoker.class));
        EasyMock.replay(beanFactory, listenerRegistry);

        subscriberProcessor.postProcessBeforeDestruction(this, "beanName");
        EasyMock.verify(beanFactory, listenerRegistry);
    }

    @Test
    public void getOrder() {
        Assert.assertEquals(Integer.MAX_VALUE - 1, subscriberProcessor.getOrder());
    }

    @Test
    public void onEvent() throws NoSuchMethodException {
        beanFactory.getBean("beanName");
        EasyMock.expectLastCall().andReturn(this).times(2);
        ConversionService conversionService = EasyMock.createMock(ConversionService.class);
        beanFactory.getBean(ConversionService.class);
        EasyMock.expectLastCall().andReturn(conversionService);
        conversionService.convert(1, String.class);
        EasyMock.expectLastCall().andReturn("1").times(2);
        EasyMock.replay(beanFactory, conversionService);

        Listener<MethodInvocationEvent> listener = subscriberProcessor.new SpringBeanMethodInvoker("beanName", this
                .getClass().getMethod("subscriberMethod", new Class<?>[] { String.class }));
        listener.onEvent(new MethodInvocationEvent("sync", this.getClass(), this.getClass().getMethod(
                "publisherMethod", new Class<?>[] { int.class }), 1));
        // Just for test cache of conversion service
        listener.onEvent(new MethodInvocationEvent("sync", this.getClass(), this.getClass().getMethod(
                "publisherMethod", new Class<?>[] { int.class }), 1));
        EasyMock.verify(beanFactory, conversionService);
    }

    @Subscriber("event")
    public void subscriberMethod(String string) {
    }

    public void publisherMethod(int number) {
    }
}
