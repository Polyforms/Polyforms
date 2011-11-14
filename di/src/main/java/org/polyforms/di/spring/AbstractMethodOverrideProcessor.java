package org.polyforms.di.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.beans.factory.support.MethodReplacer;
import org.springframework.beans.factory.support.ReplaceOverride;
import org.springframework.util.ClassUtils;

/**
 * {@link BeanFactoryPostProcessor} to add method overrides for instantiation interfaces or abstract classes, and all
 * invocation of methods in that classes of interfaces would be implemented with an aspect.
 * 
 * The typical usage of that incldes two cases: The typical usage includes two cases:
 * <p>
 * <bean id="myInterface" class="package.name.MyInterface" />
 * <p>
 * <bean id="myAbstractObject" class="package.name.MyAbstractObject" />
 * 
 * As there is no default implementation for abstract methods in interfaces or abstract classes, use an interceptor to
 * take over the execution of these methods, otherwise, exceptions would be thrown by invocation of these methods.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class AbstractMethodOverrideProcessor implements BeanFactoryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMethodOverrideProcessor.class);
    private static final String UNSUPPORTED_METHOD_REPLACER_NAME = "__UNSUPPORTED_METHOD_REPLACER";

    /**
     * {@inheritDoc}
     */
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) {
        for (final String beanName : beanFactory.getBeanDefinitionNames()) {
            final AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory
                    .getBeanDefinition(beanName);
            if (!beanDefinition.isAbstract()) {
                final Class<?> clazz = resolveBeanClass(beanFactory, beanDefinition);
                if (clazz != null && Modifier.isAbstract(clazz.getModifiers())) {
                    registerMethodReplacerIfNecessary(beanFactory);
                    addMethodOverrides(beanDefinition, clazz);
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

    private synchronized void registerMethodReplacerIfNecessary(final ConfigurableListableBeanFactory beanFactory) {
        if (!beanFactory.containsBean(UNSUPPORTED_METHOD_REPLACER_NAME)) {
            beanFactory.registerSingleton(UNSUPPORTED_METHOD_REPLACER_NAME, new UnsupportedMethodReplacer());
        }
    }

    private void addMethodOverrides(final AbstractBeanDefinition beanDefinition, final Class<?> clazz) {
        final MethodOverrides methodOverrides = beanDefinition.getMethodOverrides();
        for (final Method method : clazz.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers()) && methodOverrides.getOverride(method) == null) {
                LOGGER.info("Add override for {}.", method);
                methodOverrides.addOverride(new ReplaceOverride(method.getName(), UNSUPPORTED_METHOD_REPLACER_NAME));
            }
        }
    }

    protected static final class UnsupportedMethodReplacer implements MethodReplacer {
        /**
         * {@inheritDoc}
         */
        public Object reimplement(final Object instance, final Method method, final Object[] arguments) {
            throw new UnsupportedOperationException("Invocation of method[" + method
                    + "] is disallowed beacause it is in an interface or abstract class without implementation. "
                    + "Do you configurate interceptor for this method?");
        }
    }
}
