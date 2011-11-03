package org.polyforms.delegation;

import org.polyforms.delegation.builder.DelegationBuilderHolder;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.parameter.provider.ArgumentAt;
import org.polyforms.parameter.provider.ArgumentOfType;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.polyforms.parameter.provider.ConstantArgument;
import org.polyforms.util.DefaultValue;

/**
 * Abstract implementstion for {@link DelegationRegister} whose methods handling {@link argumentProvider}.
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
        return provideBy(targetType, new ArgumentAt(position));
    }

    protected final <P> P typeOf(final Class<P> targetType, final Class<?> sourceType) {
        return provideBy(targetType, new ArgumentOfType(sourceType));
    }

    @SuppressWarnings("unchecked")
    protected final <P> P constant(final P value) {
        return provideBy((Class<P>) (value == null ? null : value.getClass()), new ConstantArgument(value));
    }

    protected final <P> P provideBy(final Class<P> type, final ArgumentProvider argumentProvider) {
        DelegationBuilderHolder.get().parameter(argumentProvider);
        return DefaultValue.get(type);
    }
}
