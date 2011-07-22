package org.polyforms.di.spring.schema;

import org.polyforms.di.spring.AbstractMethodOverrideProcessor;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

final class PolyformsClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    protected PolyformsClassPathBeanDefinitionScanner(final BeanDefinitionRegistry registry,
            final boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
        registerNullBeanIfNecessary(registry);
    }

    @Override
    protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }

    private void registerNullBeanIfNecessary(final BeanDefinitionRegistry registry) {
        final String beanName = "abstractMethodOverrideProcessor";
        if (!registry.containsBeanDefinition(beanName)) {
            final RootBeanDefinition beanDefinition = new RootBeanDefinition(AbstractMethodOverrideProcessor.class);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
