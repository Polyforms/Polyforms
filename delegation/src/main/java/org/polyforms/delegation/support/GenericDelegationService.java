package org.polyforms.delegation.support;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.springframework.core.convert.ConversionService;

/**
 * Generic implementation of {@link DelegationService}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class GenericDelegationService implements DelegationService {
    private final DelegationExecutorFactory executorFactory;
    private final DelegationRegistry delegationRegistry;

    /**
     * Create an instance with {@link ConversionService}, {@link BeanContainer} and {@link DelegationRegistry}.
     */
    @Inject
    public GenericDelegationService(final ConversionService conversionService, final BeanContainer beanContainer,
            final DelegationRegistry delegationRegistry) {
        executorFactory = new DelegationExecutorFactory(conversionService, beanContainer);
        this.delegationRegistry = delegationRegistry;
    }

    /**
     * {@inheritDoc}
     */
    public boolean canDelegate(final Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Parameter method (Method) must not be null.");
        }

        return delegationRegistry.contains(method);
    }

    /**
     * {@inheritDoc}
     */
    public Object delegate(final Object target, final Method delegator, final Object... arguments) throws Throwable {
        final Delegation delegationPair = delegationRegistry.get(delegator);
        return executorFactory.getDelegationExecutor(delegationPair).execute(target, delegationPair, arguments);
    }
}
