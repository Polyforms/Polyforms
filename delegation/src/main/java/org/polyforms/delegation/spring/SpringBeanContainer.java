package org.polyforms.delegation.spring;

import org.polyforms.delegation.builder.BeanContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapter implementation of {@link BeanContainer} for Spring {@link org.springframework.beans.factory.BeanFactory}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class SpringBeanContainer implements BeanContainer {
    private final BeanFactory beanFactory;

    /**
     * Create an instance with Spring {@link BeanFactory}
     */
    @Autowired
    public SpringBeanContainer(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsBean(final Class<?> type) {
        try {
            beanFactory.getBean(type);
            return true;
        } catch (final BeansException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getBean(final Class<T> type) {
        return beanFactory.getBean(type);
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getBean(final String name, final Class<T> type) {
        return beanFactory.getBean(name, type);
    }
}
