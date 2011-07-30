package org.polyforms.delegation;

import org.polyforms.delegation.builder.DelegationBuilderFactory;

/**
 * The interface is used to create delegation programmatically.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationRegister {
    /**
     * Create delegations with help by {@link DelegationBuilderFactory}.
     * 
     * @param delegationBuilder builder of delegation
     */
    void registerDelegations(DelegationBuilderFactory delegationBuilder);
}
