package org.polyforms.delegation.spring;

import java.lang.reflect.Method;
import java.util.Collection;

import org.polyforms.delegation.Delegate;
import org.polyforms.delegation.DelegationRegister;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;
import org.polyforms.di.spring.util.support.GenericBeanFactoryVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * {@link BeanDefinitionRegistryPostProcessor} which executing {@link DelegationBuilder} to bind delegator and delegatee
 * and register delegator as a bean if necessary.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class DelegationRegisterProcessor implements BeanFactoryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelegationRegisterProcessor.class);
    private final BeanFactoryVisitor beanFactoryVisitor = new GenericBeanFactoryVisitor();

    /**
     * {@inheritDoc}
     */
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final DelegationRegistry delegationRegistry = beanFactory.getBean(DelegationRegistry.class);
        final DelegationBuilder delegationBuilder = new DelegationBuilder(delegationRegistry);
        final Collection<DelegationRegister> delegationRegisters = beanFactory.getBeansOfType(DelegationRegister.class)
                .values();

        for (final DelegationRegister register : delegationRegisters) {
            LOGGER.info("Register delegations from register {}", register.getClass().getName());
            register.registerDelegations(delegationBuilder);
        }

        beanFactoryVisitor.visit(beanFactory, new AnnotatedDelegationRegister(delegationBuilder));
    }

    protected static class AnnotatedDelegationRegister implements BeanClassVisitor {
        private final DelegationBuilder delegationBuilder;

        protected AnnotatedDelegationRegister(final DelegationBuilder delegationBuilder) {
            this.delegationBuilder = delegationBuilder;
        }

        /**
         * {@inheritDoc}
         */
        public void visit(final String beanName, final AbstractBeanDefinition beanDefinition, final Class<?> clazz) {
            for (final Method method : clazz.getMethods()) {
                registerDelegate(method);
            }
        }

        private void registerDelegate(final Method method) {
            final Delegate delegateTo = method.getAnnotation(Delegate.class);
            if (delegateTo != null) {
                final String methodName = normalizeMethodName(delegateTo.methodName(), method);
                delegationBuilder.delegate(method).to(delegateTo.value(), methodName, delegateTo.parameterTypes())
                        .withName(delegateTo.name());
            }
        }

        private String normalizeMethodName(final String methodName, final Method method) {
            return StringUtils.hasText(methodName) ? methodName : method.getName();
        }
    }
}
