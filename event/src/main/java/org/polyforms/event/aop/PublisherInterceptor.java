package org.polyforms.event.aop;

import java.lang.reflect.Method;
import java.util.Locale;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.polyforms.event.Publisher;
import org.polyforms.event.Publisher.When;
import org.polyforms.event.Publishers;
import org.polyforms.event.bus.EventBus;
import org.polyforms.event.bus.support.MethodInvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link MethodInterceptor} for methods which annotated by {@link Publisher}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class PublisherInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherInterceptor.class);
    private final EventBus eventBus;

    /**
     * Create an instance with {@link EventBus}.
     */
    public PublisherInterceptor(final EventBus eventBus) {
        Assert.notNull(eventBus);
        this.eventBus = eventBus;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        Assert.notNull(methodInvocation);

        final Object target = methodInvocation.getThis();
        final Method method = methodInvocation.getMethod();
        final Object[] arguments = methodInvocation.getArguments();
        process(When.BEFORE, method, arguments, target);
        final Object returnValue = methodInvocation.proceed();
        process(When.AFTER, method, arguments, target);
        return returnValue;
    }

    private void process(final When when, final Method method, final Object[] args, final Object target) {
        final Method specificMethod = ClassUtils.getMostSpecificMethod(method, target.getClass());
        final Publishers publishers = AnnotationUtils.findAnnotation(specificMethod, Publishers.class);
        if (publishers != null) {
            for (final Publisher publisher : publishers.value()) {
                publishEvent(when, method, args, target, specificMethod, publisher);
            }
        }

        final Publisher publisher = AnnotationUtils.findAnnotation(specificMethod, Publisher.class);
        if (publisher != null) {
            publishEvent(when, method, args, target, specificMethod, publisher);
        }
    }

    private void publishEvent(final When when, final Method method, final Object[] args, final Object target,
            final Method specificMethod, final Publisher annotation) {
        if (annotation.when() == when) {
            eventBus.publish(new MethodInvocationEvent(annotation.value(), target.getClass(), specificMethod, args));
            LOGGER.debug("Publish domain event {} {} invocation of {}.", new Object[] { annotation.value(),
                    when.name().toLowerCase(Locale.getDefault()), method });
        }
    }
}
