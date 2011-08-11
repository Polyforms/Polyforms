package org.polyforms.delegation.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.polyforms.delegation.DelegationService;

/**
 * {@link org.aopalliance.intercept.Interceptor} for methods delegated by others.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class DelegationInterceptor implements MethodInterceptor {
    private final DelegationService delegationService;

    /**
     * Create an instance with {@link DelegationService}.
     */
    public DelegationInterceptor(final DelegationService delegationService) {
        this.delegationService = delegationService;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable { // SUPPRESS CHECKSTYLE
        return delegationService.delegate(methodInvocation.getThis().getClass(), methodInvocation.getMethod(),
                methodInvocation.getArguments());
    }
}
