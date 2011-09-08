package org.polyforms.delegation.builder;

/**
 * Provider used to resolve argument for delegatee method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <P> type of parameter
 */
public interface ParameterProvider<P> {
    /**
     * Check whether the argument can be resolved.
     * 
     * @param parameterTypes of delegatee method
     */
    void validate(Class<?>... parameterTypes);

    /**
     * Resolve argument from invocation arguments of delegator method.
     * 
     * @param arguments of delegator method
     * @return argument used by delegatee method
     */
    P get(Object... arguments);
}
