package org.polyforms.delegation.spring;

import java.util.Collection;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * {@link FactoryBean} which lists all beans of specific type in {@link ListableBeanFactory}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class BeanListFactoryBean<T> implements FactoryBean<Collection<T>>, BeanFactoryAware {
    private ListableBeanFactory beanFactory;

    private final Class<T> beanClass;

    private Collection<T> beans;

    /**
     * Create an instance with specific bean type.
     */
    public BeanListFactoryBean(final Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<T> getObject() {
        if (beans == null) {
            beans = beanFactory.getBeansOfType(beanClass).values();
        }
        return beans;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getObjectType() {
        return Collection.class;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}
