package org.polyforms.di.spring.util;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.util.ClassUtils;

/**
 * Utility class to iterate bean definition in Spring bean factory.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class BeanFactoryVisitor {
    protected BeanFactoryVisitor() {
        throw new UnsupportedOperationException();
    }

    /**
     * Visit all bean definition in bean factory with specific visitor.
     * 
     * @param beanFactory which is visited
     * @param visitor for bean factory
     */
    public static void visit(final ConfigurableListableBeanFactory beanFactory, final BeanClassVisitor visitor) {
        for (final String beanName : beanFactory.getBeanDefinitionNames()) {
            final AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory
                    .getBeanDefinition(beanName);

            if (!beanDefinition.isAbstract()) {
                AbstractBeanDefinition currentDefinition = beanDefinition;
                Class<?> clazz = null;
                try {
                    do {
                        clazz = currentDefinition.resolveBeanClass(ClassUtils.getDefaultClassLoader());
                        final String parentName = beanDefinition.getParentName();
                        currentDefinition = parentName == null ? null : (AbstractBeanDefinition) beanFactory
                                .getBeanDefinition(parentName);
                    } while (clazz == null && currentDefinition != null);
                } catch (final ClassNotFoundException e) {
                    // IGNORE Nothing should be done to inexistent class.
                }

                visitor.visit(beanName, beanDefinition, clazz);
            }
        }
    }

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
