package org.polyforms.delegation.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.polyforms.delegation.Delegate;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.support.DefaultDelegationBuilder;
import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;
import org.polyforms.di.spring.util.support.GenericBeanFactoryVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * {@link BeanFactoryPostProcessor} which executing {@link DelegationBuilderFactory} to bind delegator and delegatee and
 * register delegator as a bean if necessary.
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
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) {
        final DelegationRegistry delegationRegistry = beanFactory.getBean(DelegationRegistry.class);
        final DelegationBuilder delegationBuilder = new DefaultDelegationBuilder(delegationRegistry);
        final Collection<DelegationRegister> delegationRegisters = beanFactory.getBeansOfType(DelegationRegister.class)
                .values();

        for (final DelegationRegister register : delegationRegisters) {
            LOGGER.info("Register delegations from register {}", register.getClass().getName());
            register.register(delegationBuilder);
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
            final boolean annotationPresent = clazz.isAnnotationPresent(Delegate.class);
            final Set<Method> annotatedMethods = getAnnotatedMethods(clazz.getMethods());
            if (!annotationPresent && annotatedMethods.isEmpty()) {
                return;
            }

            final Object source = delegationBuilder.from(clazz);

            if (annotationPresent) {
                delegationBuilder.delegate();
            }

            for (final Method method : annotatedMethods) {
                registerDelegate(source, method);
            }

            delegationBuilder.registerDelegations();
        }

        private Set<Method> getAnnotatedMethods(final Method[] methods) {
            final Set<Method> annotatedMethods = new HashSet<Method>();
            for (final Method method : methods) {
                if (method.isAnnotationPresent(Delegate.class)) {
                    annotatedMethods.add(method);
                }
            }
            return annotatedMethods;
        }

        private void registerDelegate(final Object source, final Method method) {
            final Object[] arguments = new Object[method.getParameterTypes().length];
            try {
                method.invoke(source, arguments);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException("Should never get here");
            } catch (final InvocationTargetException e) {
                throw new IllegalStateException("Should never get here");
            }
            delegationBuilder.delegate();
        }
    }
}
