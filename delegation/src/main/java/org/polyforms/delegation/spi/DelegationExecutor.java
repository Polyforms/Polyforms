package org.polyforms.delegation.spi;

import java.util.concurrent.Executor;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;

/**
 * The interface for executing {@link Delegation} with specific arguments.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationExecutor {
    /**
     * {@link Executor} implementation which throws {@link UnsupportedOperationException}.
     * 
     * It is used for situation that no matched executor has found for specific method.
     */
    DelegationExecutor UNSUPPORTED = new DelegationExecutor() {
        /**
         * {@inheritDoc}
         */
        public Object execute(final Object target, final Delegation delegation, final Object[] arguments) {
            throw new UnsupportedOperationException(delegation + " is unsupported.");
        }
    };

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
