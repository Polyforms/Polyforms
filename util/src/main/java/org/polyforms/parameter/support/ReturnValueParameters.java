package org.polyforms.parameter.support;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;
import org.polyforms.util.ArrayUtils;
import org.springframework.util.Assert;

/**
 * {@link Parameters} to extra return parameter.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class ReturnValueParameters<P extends Parameter> implements Parameters<P> {
    private final ReturnParameterAware<P> parameters;

    /**
     * Create an instance by wrapping a {@link ReturnParameterAware}.
     */
    public ReturnValueParameters(final ReturnParameterAware<P> parameters) {
        Assert.notNull(parameters);
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    public P[] getParameters() {
        final P[] originalParameters = parameters.getParameters();
        final P returnParameter = parameters.getReturnParameter();

        if (returnParameter == null) {
            return originalParameters;
        }

        final P[] parametersWithReturnValue = ArrayUtils.copyOf(originalParameters, originalParameters.length + 1);
        parametersWithReturnValue[originalParameters.length] = returnParameter;
        return parametersWithReturnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ReturnValueParameters)) {
            return false;
        }

        return parameters.equals(((ReturnValueParameters<?>) obj).parameters);
    }
}
