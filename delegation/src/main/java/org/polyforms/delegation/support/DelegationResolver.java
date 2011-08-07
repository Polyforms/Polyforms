package org.polyforms.delegation.support;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationBuilder;

/**
 * The registry to keep all delegation built by {@link DelegationBuilder}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationResolver {
    Delegation get(Delegator delegator);

    /**
     * Check whether a delegation for specific method supports.
     * 
     * @param delegator the delegator
     * @return true if there is a delegation of specific delegator, false if not
     */
    boolean supports(Delegator delegator);
}
