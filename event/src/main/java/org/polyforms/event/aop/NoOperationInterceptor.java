package org.polyforms.event.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * {@link MethodInterceptor} for methods which annotated by {@link org.polyforms.event.annotation.NoOperation}.
 * 
 * It does nothing in execution, and the return type must be void.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class NoOperationInterceptor implements MethodInterceptor {

    /**
     * {@inheritDoc}
     */
    public Object invoke(final MethodInvocation invocation) {
        return Void.TYPE;
    }
}
