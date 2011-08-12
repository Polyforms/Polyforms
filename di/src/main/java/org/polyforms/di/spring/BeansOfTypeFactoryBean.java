package org.polyforms.di.spring;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * {@link org.springframework.beans.factory.FactoryBean} which lists all beans of specific type in
 * {@link ListableBeanFactory}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class BeansOfTypeFactoryBean<T> extends AbstractFactoryBean<Collection<T>> {
    private final Class<T> beanClass;

    /**
     * Create an instance with specific bean type.
     */
    public BeansOfTypeFactoryBean(final Class<T> beanClass) {
        super();
        this.beanClass = beanClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Collection<T> createInstance() {
        if (beanClass == null) {
            return Collections.EMPTY_LIST;
        }
        return ((ListableBeanFactory) getBeanFactory()).getBeansOfType(beanClass).values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return Collection.class;
    }
}
