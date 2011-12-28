package org.polyforms.delegation;

import org.springframework.core.GenericTypeResolver;

/**
 * A shortcut to register delegation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <S> type of delegator
 * @param <S> type of delegatee
 */
public class DelegationRegister<S, T> extends DelegatorRegister<S> {
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void register(final S source) {
        final Class<T> delegateeType = GenericTypeResolver.resolveTypeArguments(this.getClass(),
                DelegationRegister.class)[1];
        with(new DelegateeRegister<T>(delegateeType) {
        });
    }
}
