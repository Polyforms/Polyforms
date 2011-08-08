package org.polyforms.delegation.support;

import org.polyforms.delegation.builder.Delegation;

/**
 * The interface for executing {@link SimpleDelegation} with specific arguments.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationExecutor {
    /**
     * Execute a {@link SimpleDelegation} with specific arguments.
     * 
     * @param delegation the delegation which is executing
     * @param arguments passed to invoke delegator method
     * @return the return value of execution of delegatee method
     * @throws Throwable if invocation of delegatee method throw an exception
     */
    Object execute(Delegation delegation, Object... arguments) throws Throwable;
}
