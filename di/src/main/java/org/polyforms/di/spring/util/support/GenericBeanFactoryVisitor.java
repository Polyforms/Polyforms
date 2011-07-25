package org.polyforms.di.spring.util.support;

import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.util.ClassUtils;

/**
 * Utility class to iterate bean definition in Spring bean factory.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class GenericBeanFactoryVisitor implements BeanFactoryVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericBeanFactoryVisitor.class);

    /**
     * {@inheritDoc}
     */
    public void visit(final ConfigurableListableBeanFactory beanFactory, final BeanClassVisitor visitor) {
        for (final String beanName : beanFactory.getBeanDefinitionNames()) {
            final AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory
                    .getBeanDefinition(beanName);
            if (!beanDefinition.isAbstract()) {
                LOGGER.trace("Visit bean {}.", beanName);

                final Class<?> clazz = resolveBeanClass(beanFactory, beanDefinition);
                if (clazz != null) {
                    LOGGER.debug("Resolved {} of bean {}.", clazz, beanName);
                    visitor.visit(beanName, beanDefinition, clazz);
                }
            }
        }
    }

    private Class<?> resolveBeanClass(final ConfigurableListableBeanFactory beanFactory,
            final AbstractBeanDefinition beanDefinition) {
        Class<?> clazz = null;
        AbstractBeanDefinition currentDefinition = beanDefinition;

        while (true) {
            try {
                clazz = currentDefinition.resolveBeanClass(ClassUtils.getDefaultClassLoader());
            } catch (final ClassNotFoundException e) {
                break;
            }

            if (clazz != null) {
                break;
            }

            currentDefinition = getParentBeanDefinition(beanFactory, beanDefinition);
            if (currentDefinition == null) {
                break;
            }
        }

        return clazz;
    }

    private AbstractBeanDefinition getParentBeanDefinition(final ConfigurableListableBeanFactory beanFactory,
            final AbstractBeanDefinition beanDefinition) {
        final String parentName = beanDefinition.getParentName();
        return parentName == null ? null : (AbstractBeanDefinition) beanFactory.getBeanDefinition(parentName);
    }
}
