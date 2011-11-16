package org.polyforms.parameter.provider;

import java.lang.reflect.Method;

import org.polyforms.parameter.ArgumentProvider;
import org.springframework.util.Assert;

/**
 * Argument Resolved as return value of invoked method. The return value must put at the end of arguments.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class ReturnValue implements ArgumentProvider {
    private int position = -1;

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
        Assert.isTrue(method.getReturnType() != void.class, "There is no return value from " + method);
        position = method.getParameterTypes().length;
    }
}
