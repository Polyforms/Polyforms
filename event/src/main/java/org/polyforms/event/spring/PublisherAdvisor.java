package org.polyforms.event.spring;

import org.polyforms.event.Publisher;
import org.polyforms.event.Publishers;
import org.polyforms.event.aop.PublisherInterceptor;
import org.polyforms.event.bus.EventBus;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.aop.Advisor} for methods which annotated by {@link Publisher} and {@link Publishers}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public class PublisherAdvisor extends DefaultPointcutAdvisor {
    private static final long serialVersionUID = 6904896946238590335L;
    private static final int DEFAULT_ORDER = 100;

    /**
     * Create an default instance.
     */
    @Autowired
    public PublisherAdvisor(final EventBus eventBus) {
        super(new ComposablePointcut(new AnnotationMatchingPointcut(null, Publishers.class))
                .union(new AnnotationMatchingPointcut(null, Publisher.class)), new PublisherInterceptor(eventBus));
        setOrder(DEFAULT_ORDER);
    }
}
