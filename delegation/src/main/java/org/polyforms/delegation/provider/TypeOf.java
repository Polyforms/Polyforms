package org.polyforms.delegation.provider;

import java.util.ArrayList;
import java.util.List;

import org.polyforms.delegation.builder.ParameterProvider;
import org.springframework.util.Assert;

/**
 * Argument Resolved by type of arguments used to invoke delegator method.
 */
public final class TypeOf<P> implements ParameterProvider<P> {
    private final Class<?> type;

    /**
     * Create an instance with type of parameter.
     */
    public TypeOf(final Class<?> type) {
        Assert.notNull(type);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public P get(final Object... arguments) {
        for (final Object argument : arguments) {
            if (argument != null && type.isInstance(argument)) {
                return (P) argument;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Class<?>... parameterTypes) {
        final List<Class<?>> matchedParameterTypes = new ArrayList<Class<?>>();
        for (final Class<?> parameterType : parameterTypes) {
            if (type == parameterType) {
                matchedParameterTypes.add(parameterType);
            }
        }

        Assert.isTrue(matchedParameterTypes.size() == 1, "There is one and only one parameter of type " + type
                + " allowed in delegator method.");
    }
}
