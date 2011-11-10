package org.polyforms.event.spring;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.polyforms.event.Subscribe;
import org.polyforms.event.bus.Event;
import org.polyforms.event.bus.Subscriber;
import org.polyforms.event.bus.SubscriberRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ReflectionUtils;

public class SubscriberPostProcessor implements PriorityOrdered, BeanFactoryAware, DestructionAwareBeanPostProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubscriberPostProcessor.class);
    private int order = Ordered.LOWEST_PRECEDENCE - 1;
    private static BeanFactory beanFactory;
    private SubscriberRegistry subscriberRegistry;

    /**
     * {@inheritDoc}
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        process(beanName, bean, new Action() {
            public void run(String type, Subscriber subscriber, boolean async) {
                subscriberRegistry.register(type, subscriber, async);
            }
        });
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        process(beanName, bean, new Action() {
            public void run(String type, Subscriber subscriber, boolean async) {
                subscriberRegistry.unregister(type, subscriber);
            }
        });
    }

    private void process(String beanName, Object bean, Action action) {
        if (beanFactory.containsBean(beanName)) {
            LOGGER.debug("process {}.", beanName);

            Class<?> clazz = AopUtils.getTargetClass(bean);
            process(beanName, action, clazz);
            for (Class<?> interfaze : clazz.getInterfaces()) {
                process(beanName, action, interfaze);
            }
        }
    }

    private void process(String beanName, Action action, Class<?> clazz) {
        LOGGER.debug("process methods of {}.", clazz.getName());

        for (Method method : clazz.getMethods()) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);

            if (annotation != null) {
                String[] types = annotation.value();
                boolean async = annotation.async();
                for (String type : types) {
                    action.run(type, new SpringBeanMethodInvoker(beanName, method), async);
                }
            }
        }
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setBeanFactory(BeanFactory factory) throws BeansException {
        beanFactory = factory;
    }

    private interface Action {
        void run(String type, Subscriber subscriber, boolean async);
    }

    protected static class SpringBeanMethodInvoker implements Subscriber {
        private final String beanName;
        private final Method method;

        public SpringBeanMethodInvoker(final String beanName, final Method method) {
            this.beanName = beanName;
            this.method = method;
        }

        public void onEvent(Event event) {
            Object bean = beanFactory.getBean(beanName);
            try {
                method.invoke(bean, event.getArguments());
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }
        }

        public Object[] prepareParameters(Object[] parameters) {
            Class<?>[] parameterTargetTypes = GenericsUtils
                    .getParameterGenericTypes(method.getDeclaringClass(), method);
            Object[] originalParameters = Arrays.copyOf(parameters, method.getParameterTypes().length);

            BeanConverter beanConverter = getBeanConverter();
            if (beanConverter == null) {
                return originalParameters;
            }
            return beanConverter.convertBeans(originalParameters, method.getParameterTypes(), parameterTargetTypes);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + beanName.hashCode();
            result = prime * result + method.getName().hashCode();
            result = prime * result + Arrays.hashCode(method.getParameterTypes());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            SpringBeanMethodInvoker other = (SpringBeanMethodInvoker) obj;
            if (!beanName.equals(other.beanName)) {
                return false;
            }

            if (!method.getName().equals(other.method.getName())) {
                return false;
            }

            return Arrays.equals(method.getParameterTypes(), other.method.getParameterTypes());
        }
    }
}
