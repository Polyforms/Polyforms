package org.polyforms.delegation.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationRegistry;

/**
 * Generic implementation of {@link DelegationRegistry}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
@Singleton
public final class SimpleDelegationRegistry implements DelegationRegistry, DelegationResolver {
    private final Map<Delegator, Delegation> delegations = new HashMap<Delegator, Delegation>();

    /**
     * {@inheritDoc}
     */
    public void register(final Delegation delegation) {
        delegations.put(new Delegator(delegation.getDelegatorType(), delegation.getDelegatorMethod()), delegation);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(final Class<?> delegatorType, final Method delegatorMethod) {
        return supports(new Delegator(delegatorType, delegatorMethod));
    }

    /**
     * {@inheritDoc}
     */
    public Delegation get(final Delegator delegator) {
        return delegations.get(delegator);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supports(final Delegator delegator) {
        return delegations.containsKey(delegator);
    }
}
