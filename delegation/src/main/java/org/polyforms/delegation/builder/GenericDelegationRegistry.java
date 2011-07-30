package org.polyforms.delegation.builder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Generic implementation of {@link DelegationRegistry}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
@Singleton
public final class GenericDelegationRegistry implements DelegationRegistry {
    private final Map<Method, Delegation> delegations = new HashMap<Method, Delegation>();

    /**
     * {@inheritDoc}
     */
    public void register(final Delegation delegation) {
        delegations.put(delegation.getDelegator(), delegation);
    }

    /**
     * {@inheritDoc}
     */
    public Delegation get(final Method method) {
        return delegations.get(method);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(final Method method) {
        return delegations.containsKey(method);
    }
}
