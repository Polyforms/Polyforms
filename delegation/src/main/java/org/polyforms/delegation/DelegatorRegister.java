package org.polyforms.delegation;

import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationBuilderHolder;
import org.springframework.core.GenericTypeResolver;

@SuppressWarnings("unchecked")
public abstract class DelegatorRegister<S> extends ParameterAwareRegister<S> {
    private final Class<S> delegatorType;
    private S source;

    protected DelegatorRegister() {
        delegatorType = (Class<S>) GenericTypeResolver.resolveTypeArgument(this.getClass(), DelegatorRegister.class);
    }

    protected final <T> T delegate() {
        return getBuilder().<T> delegate();
    }

    protected final <T> T delegate(final Object delegator) {
        return this.<T> delegate();
    }

    protected final <T> void with(final DelegateeRegister<T> delegateeRegister) {
        delegateeRegister.register(getSource());
        getBuilder().registerDelegations();
    }

    private S getSource() {
        if (source == null) {
            source = getBuilder().from(delegatorType);
        }
        return source;
    }

    private DelegationBuilder getBuilder() {
        return DelegationBuilderHolder.get();
    }

    protected abstract class DelegateeRegister<T> extends ParameterAwareRegister<S> {
        private final T target;

        protected DelegateeRegister(final String name) {
            this();
            getBuilder().withName(name);
        }

        protected DelegateeRegister() {
            final Class<T> delegateeType = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(),
                    DelegateeRegister.class);
            target = getBuilder().to(delegateeType);
        }

        protected final T delegate() {
            getBuilder().delegate();
            return target;
        }

        protected final T delegate(final Object delegator) {
            return delegate();
        }

        private DelegationBuilder getBuilder() {
            return DelegationBuilderHolder.get();
        }
    }
}
