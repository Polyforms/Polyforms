package org.polyforms.parameter.provider;

import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

/**
 * Argument Resolved by name of arguments which are used to invoke method.
 */
public class ArgumentNamed implements ArgumentProvider {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final String name;
    private int position = -1;

    /**
     * Create an instance with name of parameter.
     */
    public ArgumentNamed(final String name) {
        Assert.hasText(name);
        this.name = name;
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
        final String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Assert.notNull(parameterNames,
                "Cannot get parameter names because the class file was compiled without debug information.");
        Assert.isTrue(position < parameterNames.length, "Parameter position " + position
                + " must not less than parameter count " + parameterNames.length + " of delegator method.");
        for (int i = 0; i < parameterNames.length; i++) {
            if (name.equals(parameterNames[i])) {
                position = i;
                break;
            }
        }
    }
}
