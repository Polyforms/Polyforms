package org.polyforms.event.spring;

import java.lang.reflect.Method;

import org.polyforms.event.Subscriber;
import org.polyforms.event.bus.Listener;
import org.polyforms.event.bus.ListenerRegistry;
import org.polyforms.event.bus.support.MethodInvocationEvent;
import org.polyforms.parameter.ArgumentProvider;
import org.polyforms.parameter.ParameterMatcher;
import org.polyforms.parameter.support.MethodParameter;
import org.polyforms.parameter.support.MethodParameterMatcher;
import org.polyforms.parameter.support.MethodParameters;
import org.polyforms.parameter.support.ReturnValueParameters;
import org.polyforms.util.ConversionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * BeanPostProcessor to register and unregister event subscribers annotated by {@link Subscriber}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public class SubscriberProcessor implements PriorityOrdered, BeanFactoryAware, DestructionAwareBeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberProcessor.class);
    private final int order = Ordered.LOWEST_PRECEDENCE - 1;
    private final ParameterMatcher<MethodParameter, MethodParameter> parameterMatcher = new MethodParameterMatcher();
    private ConversionService conversionService;
    private ListenerRegistry listenerRegistry;
    private BeanFactory beanFactory;

    /**
     * {@inheritDoc}
     */
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) {
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    public Object postProcessAfterInitialization(final Object bean, final String beanName) {
        process(beanName, bean, new Action() {
            @Override
            protected void run(final String type, final Listener<?> subscriber, final boolean async) {
                getListenerRegistry().register(type, subscriber, async);
                LOGGER.info("Register listener {} to {}.", new Object[] { subscriber, type });
            }
        });
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    public void postProcessBeforeDestruction(final Object bean, final String beanName) {
        process(beanName, bean, new Action() {
            @Override
            protected void run(final String type, final Listener<?> subscriber, final boolean async) {
                getListenerRegistry().unregister(type, subscriber);
                LOGGER.info("unregister listener {} from {}.", subscriber, type);
            }
        });
    }

    private void process(final String beanName, final Object bean, final Action action) {
        if (beanFactory.containsBean(beanName)) {
            LOGGER.debug("process {}.", beanName);
            process(beanName, action, bean.getClass());
        }
    }

    private void process(final String beanName, final Action action, final Class<?> clazz) {
        LOGGER.debug("process methods of {}.", clazz.getName());

        for (final Method method : clazz.getMethods()) {
            final Subscriber subsciber = AnnotationUtils.findAnnotation(method, Subscriber.class);

            if (subsciber != null) {
                final String[] types = subsciber.value();
                final boolean async = subsciber.async();
                for (final String type : types) {
                    action.run(type, new SpringBeanMethodInvoker(beanName, method), async);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOrder() {
        return order;
    }

    /**
     * {@inheritDoc}
     */
    public void setBeanFactory(final BeanFactory factory) {
        beanFactory = factory;
    }

    private ListenerRegistry getListenerRegistry() {
        if (listenerRegistry == null) {
            listenerRegistry = beanFactory.getBean(ListenerRegistry.class);
        }
        return listenerRegistry;
    }

    private abstract static class Action {
        protected abstract void run(String type, Listener<?> subscriber, boolean async);
    }

    protected class SpringBeanMethodInvoker implements Listener<MethodInvocationEvent> {
        private final String beanName;
        private final Method method;

        protected SpringBeanMethodInvoker(final String beanName, final Method method) {
            this.beanName = beanName;
            this.method = method;
        }

        /**
         * {@inheritDoc}
         */
        public void onEvent(final MethodInvocationEvent event) {
            final Object bean = beanFactory.getBean(beanName);
            final Object[] convertedArguments = convertArguments(event, bean);
            ReflectionUtils.invokeMethod(method, bean, convertedArguments);
        }

        private Object[] convertArguments(final MethodInvocationEvent event, final Object bean) {
            final Object[] matchedArguments = matchParameters(event, bean);

            final Object[] convertedArguments = ConversionUtils.convertArguments(getConversionService(),
                    bean.getClass(), method, matchedArguments);

            return convertedArguments;
        }

        private Object[] matchParameters(final MethodInvocationEvent event, final Object bean) {
            final ArgumentProvider[] argumentsProviders = parameterMatcher.match(
                    new ReturnValueParameters<MethodParameter>(new MethodParameters(event.getTargetClass(), event
                            .getMethod())), new MethodParameters(bean.getClass(), method));

            final Object[] arguments = event.getArguments();
            final Object[] matchedArguments = new Object[argumentsProviders.length];
            for (int i = 0; i < matchedArguments.length; i++) {
                matchedArguments[i] = argumentsProviders[i].get(arguments);
            }

            return matchedArguments;
        }

        private ConversionService getConversionService() {
            if (conversionService == null) {
                conversionService = beanFactory.getBean(ConversionService.class);
            }
            return conversionService;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + beanName.hashCode();
            result = prime * result + method.hashCode();
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof SpringBeanMethodInvoker)) {
                return false;
            }

            final SpringBeanMethodInvoker other = (SpringBeanMethodInvoker) obj;
            return beanName.equals(other.beanName) && method.equals(other.method);
        }
    }
}
