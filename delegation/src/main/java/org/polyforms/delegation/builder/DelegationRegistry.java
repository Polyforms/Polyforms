package org.polyforms.delegation.builder;

/**
 * The registry to keep all delegation built by {@link DelegationBuilder}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationRegistry {
    /**
     * Register a delegation. The delegation of delegator would be overrided if it existed.
     * 
     * @param delegation the delegation will be registed
     */
    void register(Delegation delegation);

    Delegation get(Delegator delegator);

    /**
     * Check whether a delegation for specific method supports.
     * 
     * @param delegator the delegator
     * @return true if there is a delegation of specific delegator, false if not
     */
    boolean supports(Delegator delegator);
}
