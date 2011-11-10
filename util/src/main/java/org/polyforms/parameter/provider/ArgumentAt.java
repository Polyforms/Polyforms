package org.polyforms.parameter.provider;

import java.lang.reflect.Method;

import org.polyforms.parameter.ArgumentProvider;
import org.springframework.util.Assert;

/**
 * Argument Resolved by position of arguments which are used to invoke method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class ArgumentAt implements ArgumentProvider {
    private final int position;

    /**
     * Create an instance with position of parameter.
     */
    public ArgumentAt(final int position) {
        Assert.isTrue(position >= 0);
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object... arguments) {
        return arguments[position];
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        Assert.isTrue(position < parameterTypes.length, "Parameter position " + position
                + " must not less than parameter count " + parameterTypes.length + " of delegator method.");
    }
}
