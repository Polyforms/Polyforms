package org.polyforms.delegation.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.Delegator;
import org.polyforms.delegation.util.AopUtils;
import org.springframework.util.ClassUtils;

/**
 * Generic implementation of {@link DelegationService}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class GenericDelegationService implements DelegationService {
    private final Map<Delegator, Delegator> delegatorMappingCache = new HashMap<Delegator, Delegator>();
    private final DelegationExecutor delegationExecutor;
    private final DelegationRegistry delegationRegistry;

    /**
     * Create an instance with {@link DelegationExecutor} and {@link DelegationRegistry}.
     */
    @Inject
    public GenericDelegationService(final DelegationExecutor delegationExecutor,
            final DelegationRegistry delegationRegistry) {
        this.delegationExecutor = delegationExecutor;
        this.delegationRegistry = delegationRegistry;
    }

    /**
     * {@inheritDoc}
     */
    public boolean supports(final Class<?> delegatorType, final Method delegatorMethod) {
        if (delegatorType == null || delegatorMethod == null) {
            return false;
        }

        final Delegator originalDelegator = new Delegator(delegatorType, delegatorMethod);
        return supports(originalDelegator);
    }

    private boolean supports(final Delegator originalDelegator) {
        if (delegatorMappingCache.containsKey(originalDelegator)) {
            return true;
        }

        for (final Class<?> clazz : AopUtils.deproxy(originalDelegator.getType())) {
            final Method method = ClassUtils.getMostSpecificMethod(originalDelegator.getMethod(), clazz);
            final Delegator delegator = new Delegator(clazz, method);
            if (delegationRegistry.supports(delegator)) {
                delegatorMappingCache.put(originalDelegator, delegator);
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Object delegate(final Class<?> delegatorType, final Method delegatorMethod, final Object... arguments)
            throws Throwable {
        if (delegatorType == null) {
            throw new IllegalArgumentException("Parameter delegatorType (Class<?>) must not be null.");
        }
        if (delegatorMethod == null) {
            throw new IllegalArgumentException("Parameter delegatorType (Class<?>) must not be null.");
        }

        final Delegator originalDelegator = new Delegator(delegatorType, delegatorMethod);
        if (!delegatorMappingCache.containsKey(originalDelegator) && !supports(originalDelegator)) {
            throw new IllegalArgumentException(
                    "The delegation of {} in {} is not supported. You can use 'supports' method to check whether a delegation is supported.");
        }
        final Delegation delegation = delegationRegistry.get(delegatorMappingCache.get(originalDelegator));
        return delegationExecutor.execute(delegation, arguments);
    }
}
