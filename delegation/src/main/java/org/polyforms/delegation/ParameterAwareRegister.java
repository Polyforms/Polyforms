package org.polyforms.delegation;

import org.polyforms.delegation.builder.DelegationBuilderHolder;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.ParameterProvider;
import org.polyforms.delegation.provider.At;
import org.polyforms.delegation.provider.Constant;
import org.polyforms.delegation.provider.TypeOf;
import org.polyforms.util.DefaultValue;

/**
 * Abstract implementstion for {@link DelegationRegister} which methods handling {@link ParameterProvider}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <S> type of delegator
 */
public abstract class ParameterAwareRegister<S> implements DelegationRegister<S> {
    /**
     * {@inheritDoc}
     */
    public void register(final S source) {
    }

    protected void map(final Class<? extends Throwable> sourceType, final Class<? extends Throwable> targetType) {
        DelegationBuilderHolder.get().map(sourceType, targetType);
    }

    protected final <P> P at(final Class<P> targetType, final int position) {
        return provideBy(targetType, new At<P>(position));
    }

    protected final <P> P typeOf(final Class<P> targetType, final Class<?> sourceType) {
        return provideBy(targetType, new TypeOf<P>(sourceType));
    }

    @SuppressWarnings("unchecked")
    protected final <P> P constant(final P value) {
        return provideBy((Class<P>) (value == null ? null : value.getClass()), new Constant<P>(value));
    }

    protected final <P> P provideBy(final Class<P> type, final ParameterProvider<P> parameterProvider) {
        DelegationBuilderHolder.get().parameter(parameterProvider);
        return DefaultValue.get(type);
    }
}
