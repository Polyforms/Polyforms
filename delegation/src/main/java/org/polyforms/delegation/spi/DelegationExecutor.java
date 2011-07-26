package org.polyforms.delegation.spi;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;

/**
 * The interface for executing {@link Delegation} with specific arguments.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationExecutor {
    /**
     * Execute a {@link Delegation} with specific arguments.
     * 
     * @param delegation the delegation which is executing
     * @param arguments passed to invoke delegator method
     * @return the return value of execution of delegatee method
     * @throws Throwable if invocation of delegatee method throw an exception
     */
    Object execute(Object target, Delegation delegation, Object[] arguments) throws Throwable;
}
