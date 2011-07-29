package org.polyforms.delegation.builder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic implementation of {@link DelegationRegistry}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
@Singleton
public final class GenericDelegationRegistry implements DelegationRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDelegationRegistry.class);
    private final Map<Method, Delegation> delegators = new HashMap<Method, Delegation>();

    /**
     * {@inheritDoc}
     */
    public void register(final Delegation delegation) {
        final Method delegator = delegation.getDelegator();
        if (contains(delegator)) {
            LOGGER.warn("Delegatee {} of {} is overrided by new delegatee {}.", new Object[] {
                    get(delegator).getDelegatee(), delegator, delegation.getDelegatee() });
        }
        delegators.put(delegator, delegation);
    }

    /**
     * {@inheritDoc}
     */
    public Delegation get(final Method method) {
        return delegators.get(method);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(final Method method) {
        return delegators.containsKey(method);
    }
}
