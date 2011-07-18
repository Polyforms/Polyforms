package org.polyforms.repository.spi;

import java.lang.reflect.Method;

/**
 * Interface for executing methods in Repository.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface Executor {
    /**
     * {@link Executor} implementation which throws {@link UnsupportedOperationException}.
     * 
     * It is used for situation that no matched executor has found for specific method.
     */
    public Executor UNSUPPORTED = new Executor() {
        /**
         * {@inheritDoc}
         */
        public Object execute(final Object target, final Method method, final Object... arguments) {
            throw new UnsupportedOperationException(method + " is unsupported by repository interceptor");
        }
    };

    /**
     * Execute the delegating method with this executor with specific arguments.
     * 
     * @param target which method is executing on
     * @param method to be executed by executor
     * @param arguments passed by client invocation
     * 
     * @return the return value from execution
     */
    Object execute(Object target, Method method, Object... arguments);
}
