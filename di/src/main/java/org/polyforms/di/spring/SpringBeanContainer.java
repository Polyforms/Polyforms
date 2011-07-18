package org.polyforms.di.spring;

import java.util.Collection;

import org.polyforms.di.container.BeanContainer;
import org.polyforms.di.container.BeanNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
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
    private final ListableBeanFactory beanFactory;

    /**
     * Create an instance with Spring {@link ListableBeanFactory}
     */
    @Autowired
    public SpringBeanContainer(final ListableBeanFactory beanFactory) {
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
        try {
            return beanFactory.getBean(type);
        } catch (final BeansException e) {
            throw new BeanNotFoundException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getBean(final String name, final Class<T> type) {
        try {
            return beanFactory.getBean(name, type);
        } catch (final BeansException e) {
            throw new BeanNotFoundException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> Collection<T> getBeans(final Class<T> type) {
        return beanFactory.getBeansOfType(type).values();
    }
}
