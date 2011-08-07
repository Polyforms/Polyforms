package org.polyforms.delegation.builder;

import java.lang.reflect.Method;

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

    /**
     * Check whether a delegation for specific method supports.
     * 
     * @param delegatorType the delegator class
     * @param delegatorMethod the delegator method
     * @return true if there is a delegation of specific delegator, false if not
     */
    boolean supports(Class<?> delegatorType, Method delegatorMethod);
}
