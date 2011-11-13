package org.polyforms.event.spring;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.event.Publisher;
import org.polyforms.event.Publishers;
import org.polyforms.event.aop.PublisherInterceptor;
import org.polyforms.event.bus.EventBus;
import org.springframework.aop.MethodMatcher;

public class PublisherAdvisorTest {
    private PublisherAdvisor publisherAdvisor;

    @Before
    public void setUp() {
        publisherAdvisor = new PublisherAdvisor(EasyMock.createMock(EventBus.class));
    }

    @Test
    public void advice() {
        Assert.assertTrue(publisherAdvisor.getAdvice() instanceof PublisherInterceptor);
    }

    @Test
    public void matchePublishers() throws NoSuchMethodException {
        final MethodMatcher methodMatcher = publisherAdvisor.getPointcut().getMethodMatcher();
        Assert.assertTrue(methodMatcher.matches(MockInterface.class.getMethod("publishers", new Class<?>[0]),
                MockInterface.class));
    }

    @Test
    public void matchePublisher() throws NoSuchMethodException {
        final MethodMatcher methodMatcher = publisherAdvisor.getPointcut().getMethodMatcher();
        Assert.assertTrue(methodMatcher.matches(MockInterface.class.getMethod("publisher", new Class<?>[0]),
                MockInterface.class));
    }

    @Test
    public void notMatches() throws NoSuchMethodException {
        final MethodMatcher methodMatcher = publisherAdvisor.getPointcut().getMethodMatcher();
        Assert.assertFalse(methodMatcher.matches(MockInterface.class.getMethod("normal", new Class<?>[0]),
                MockInterface.class));
    }

    public interface MockInterface {
        @Publishers(@Publisher("name"))
        void publishers();

        @Publisher("name")
        void publisher();

        void normal();
    }
}
