package org.polyforms.di.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.beans.factory.support.MethodReplacer;
import org.springframework.beans.factory.support.ReplaceOverride;

/**
 * {@link BeanFactoryPostProcessor} to add method overrides for instantiation interfaces or abstract classes, and all
 * invocation of methods in that classes of interfaces would be implemented with an aspect.
 * 
 * The typical usage of that incldes two cases:
 * <p>
 * <bean id="myInterface" class="package.name.MyInterface" />
 * <p>
 * <bean id="myAbstractObject" class="package.name.MyAbstractObject" />
 * 
 * As there is no default implementation for abstract methods in interface or abstract classes, an intercept have to
 * take over the execution of that methods. Otherwise, some exceptions would be thrown by invocation by these methods.
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
        BeanFactoryVisitor.visit(beanFactory, new BeanClassVisitor() {
            /**
             * {@inheritDoc}
             */
            public void visit(final String beanName, final AbstractBeanDefinition beanDefinition, final Class<?> clazz) {
                if (clazz != null && Modifier.isAbstract(clazz.getModifiers())) {
                    registerMethodReplacerIfNecessary(beanFactory);
                    addMethodOverrides(beanDefinition, clazz);
                }
            }
        });
    }

    private synchronized void registerMethodReplacerIfNecessary(final ConfigurableListableBeanFactory beanFactory) {
        if (!beanFactory.containsBean(AbstractMethodOverrideProcessor.UNSUPPORTED_METHOD_REPLACER_NAME)) {
            beanFactory.registerSingleton(AbstractMethodOverrideProcessor.UNSUPPORTED_METHOD_REPLACER_NAME,
                    new UnsupportedMethodReplacer());
        }
    }

    private void addMethodOverrides(final AbstractBeanDefinition beanDefinition, final Class<?> clazz) {
        final MethodOverrides methodOverrides = beanDefinition.getMethodOverrides();
        for (final Method method : clazz.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers()) && methodOverrides.getOverride(method) == null) {
                traceOverride(clazz, method);
                methodOverrides.addOverride(new ReplaceOverride(method.getName(),
                        AbstractMethodOverrideProcessor.UNSUPPORTED_METHOD_REPLACER_NAME));
            }
        }
    }

    private void traceOverride(final Class<?> clazz, final Method method) {
        if (clazz.isInterface()) {
            AbstractMethodOverrideProcessor.LOGGER.trace("Add override for method {} in interface {}.",
                    method.getName(), clazz.getName());
        } else {
            AbstractMethodOverrideProcessor.LOGGER.trace("Add override for method {} in abstract class {}.",
                    method.getName(), clazz.getName());
        }
    }

    private static final class UnsupportedMethodReplacer implements MethodReplacer {
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
