package org.polyforms.parameter.provider;

import java.lang.reflect.Method;


/**
 * Constant holder always returning constant value.
 */
public final class ConstantArgument implements ArgumentProvider {
    private final Object value;

    /**
     * Create an instance with constant.
     */
    public ConstantArgument(final Object value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object... arguments) {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Method method) {
    }
}
