package org.polyforms.parameter.provider;

import java.lang.reflect.Method;

import org.springframework.util.Assert;

/**
 * Argument Resolved by type of arguments which are used to invoke method.
 */
public final class ArgumentOfType implements ArgumentProvider {
    private final Class<?> type;
    private int position = -1;

    /**
     * Create an instance with type of parameter.
     */
    public ArgumentOfType(final Class<?> type) {
        Assert.notNull(type);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object... arguments) {
        Assert.isTrue(position >= 0, "Please invoke method validate before get.");
        return arguments[position];
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (type == parameterType) {
                Assert.isTrue(position < 0, "There is more than one parameter of type " + type
                        + " in delegator method.");
                position = i;
            }
        }
        Assert.isTrue(position >= 0, "There is no parameter of type " + type + " in delegator method.");
    }
}
