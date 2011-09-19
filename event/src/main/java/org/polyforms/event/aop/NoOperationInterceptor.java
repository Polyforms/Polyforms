package org.polyforms.event.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.polyforms.util.DefaultValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link MethodInterceptor} for methods which annotated by {@link org.polyforms.event.NoOperation}.
 * 
 * It does nothing in execution.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class NoOperationInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoOperationInterceptor.class);

    /**
     * {@inheritDoc}
     */
    public Object invoke(final MethodInvocation invocation) {
        final Method method = invocation.getMethod();
        LOGGER.debug("Execute no operation for {}.", method);
        return DefaultValue.get(method.getReturnType());
    }
}
