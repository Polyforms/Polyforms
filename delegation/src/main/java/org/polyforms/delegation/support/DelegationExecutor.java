package org.polyforms.delegation.support;

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
     * @param delegatorClass the type of delegator
     * @param arguments passed to invoke delegator method
     * @return the return value of execution of delegatee method
     * @throws Throwable if invocation of delegatee method throw an exception
     */
    Object execute(Delegation delegation, Class<?> delegatorClass, Object[] arguments) throws Throwable;
}
