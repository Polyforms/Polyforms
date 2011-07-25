package org.polyforms.delegation.spring;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.polyforms.delegation.annotation.DelegateTo;
import org.polyforms.delegation.annotation.DelegatedBy;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;
import org.polyforms.di.spring.util.support.GenericBeanFactoryVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * {@link BeanDefinitionRegistryPostProcessor} which executing {@link DelegationBuilder} to bind delegator and delegatee
 * and register delegator as a bean if necessary.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class DelegationProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelegationProcessor.class);

    /**
     * {@inheritDoc}
     */
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) {
        final ListableBeanFactory beanFactory = (ListableBeanFactory) registry;
        final DelegationRegistry delegationRegistry = getDelegationRegistry(beanFactory);
        registerDelegations(beanFactory, delegationRegistry);
        registerBeansIfNecessary(registry, delegationRegistry);
    }

    private void registerDelegations(final ListableBeanFactory beanFactory, final DelegationRegistry delegationRegistry) {
        final Collection<DelegationBuilder> delegationBuilders = beanFactory.getBeansOfType(DelegationBuilder.class)
                .values();
        for (final DelegationBuilder builder : delegationBuilders) {
            LOGGER.info("Register delegations from builder {}", builder.getClass().getName());
            builder.registerDelegations(delegationRegistry);
        }
    }

    private void registerBeansIfNecessary(final BeanDefinitionRegistry registry,
            final DelegationRegistry delegationRegistry) {
        final Set<Class<?>> classesForRegistration = new HashSet<Class<?>>();
        for (final Delegation delegationPair : delegationRegistry.getAll()) {
            classesForRegistration.add(delegationPair.getDelegator().getDeclaringClass());
        }
        for (final Class<?> clazz : classesForRegistration) {
            LOGGER.info("Register bean for delegator {}", clazz.getName());
            registry.registerBeanDefinition(buildDefaultBeanName(clazz), new RootBeanDefinition(clazz));
        }
    }

    private String buildDefaultBeanName(final Class<?> clazz) {
        final String shortClassName = ClassUtils.getShortName(clazz);
        return StringUtils.uncapitalize(shortClassName);
    }

    private DelegationRegistry getDelegationRegistry(final ListableBeanFactory beanFactory) {
        return beanFactory.getBean(DelegationRegistry.class);
    }

    /**
     * {@inheritDoc}
     */
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) {
        new AnnotatedDelegationBuilder(beanFactory).registerDelegations(getDelegationRegistry(beanFactory));
    }

    private static final class AnnotatedDelegationBuilder extends DelegationBuilder {
        private final ConfigurableListableBeanFactory beanFactory;
        private final BeanFactoryVisitor beanFactoryVisitor = new GenericBeanFactoryVisitor();

        private AnnotatedDelegationBuilder(final ConfigurableListableBeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        protected void registerDelegations() {
            beanFactoryVisitor.visit(beanFactory, new BeanClassVisitor() {
                /**
                 * {@inheritDoc}
                 */
                public void visit(final String beanName, final AbstractBeanDefinition beanDefinition,
                        final Class<?> clazz) {
                    for (final Method method : clazz.getMethods()) {
                        registerDelegatedBy(beanName, method);
                        registerDelegatTo(method);
                    }
                }
            });
        }

        private void registerDelegatTo(final Method method) {
            final DelegateTo delegateTo = method.getAnnotation(DelegateTo.class);
            if (delegateTo != null) {
                final String methodName = normalizeMethodName(delegateTo.methodName(), method);
                delegate(method).to(delegateTo.value(), methodName, delegateTo.parameterTypes()).withName(
                        delegateTo.name());
            }
        }

        private void registerDelegatedBy(final String beanName, final Method method) {
            final DelegatedBy delegatedBy = method.getAnnotation(DelegatedBy.class);
            if (delegatedBy != null) {
                final String methodName = normalizeMethodName(delegatedBy.methodName(), method);
                delegate(delegatedBy.value(), methodName, delegatedBy.parameterTypes()).to(method).withName(beanName);
            }
        }

        private String normalizeMethodName(final String methodName, final Method method) {
            return StringUtils.hasText(methodName) ? methodName : method.getName();
        }
    }
}
