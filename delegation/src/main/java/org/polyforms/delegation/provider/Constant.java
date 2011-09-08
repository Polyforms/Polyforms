package org.polyforms.delegation.provider;

import org.polyforms.delegation.builder.ParameterProvider;

/**
 * Constant holder always returning constant value.
 */
public final class Constant<P> implements ParameterProvider<P> {
    private final P value;

    /**
     * Create an instance with constant.
     */
    public Constant(final P value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    public P get(final Object... arguments) {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Class<?>... parameterType) {
    }
}
