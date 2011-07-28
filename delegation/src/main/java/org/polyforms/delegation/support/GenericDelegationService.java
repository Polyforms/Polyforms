package org.polyforms.delegation.support;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.DelegationRegistry.Delegation;

/**
 * Generic implementation of {@link DelegationService}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class GenericDelegationService implements DelegationService {
    private final DelegationExecutor delegationExecutor;
    private final DelegationRegistry delegationRegistry;

    /**
     * Create an instance with {@link DelegationExecutorFinder} and {@link DelegationRegistry}.
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
    public boolean canDelegate(final Method method) {
        if (method == null) {
            return false;
        }

        return delegationRegistry.contains(method);
    }

    /**
     * {@inheritDoc}
     */
    public Object delegate(final Object target, final Method delegator, final Object... arguments) throws Throwable {
        final Delegation delegationPair = delegationRegistry.get(delegator);
        return delegationExecutor.execute(delegationPair, target.getClass(), arguments);
    }
}
