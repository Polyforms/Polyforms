package org.polyforms.delegation.provider;

import org.polyforms.delegation.builder.ParameterProvider;
import org.springframework.util.Assert;

/**
 * Argument Resolved by postion of arguments used to invoke delegator method.
 */
public final class At<P> implements ParameterProvider<P> {
    private final int position;

    /**
     * Create an instance with position of parameter.
     */
    public At(final int position) {
        Assert.isTrue(position >= 0);
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public P get(final Object... arguments) {
        return (P) arguments[position];
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Class<?>... parameterType) {
        Assert.isTrue(position < parameterType.length, "Parameter position " + position
                + " must not less than parameter count " + parameterType.length + " of delegator method.");
    }
}
