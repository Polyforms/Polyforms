package org.polyforms.delegation.builder;

/**
 * Interface used to register delegation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <S> type of delegator
 */
public interface DelegationRegister<S> {
    /**
     * Register delegations for specified delegator type.
     * 
     * @param source proxy of delegator type
     */
    void register(final S source);
}
