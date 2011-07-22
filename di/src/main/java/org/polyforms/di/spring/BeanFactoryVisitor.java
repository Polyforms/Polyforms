package org.polyforms.di.spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * Utility class to iterate bean definition in Spring bean factory.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface BeanFactoryVisitor {
    /**
     * Visit all bean definition in bean factory with specific visitor.
     * 
     * @param beanFactory which is visited
     * @param visitor for bean factory
     */
    void visit(final ConfigurableListableBeanFactory beanFactory, final BeanClassVisitor visitor);

    /**
     * Visitor of bean definition in bean factory.
     */
    public interface BeanClassVisitor {
        /**
         * Visit single bean definition with class of it.
         * 
         * @param beanName name of the bean definition
         * @param beanDefinition bean definition
         * @param clazz class of the bean definition
         */
        void visit(final String beanName, final AbstractBeanDefinition beanDefinition, final Class<?> clazz);
    }
}
