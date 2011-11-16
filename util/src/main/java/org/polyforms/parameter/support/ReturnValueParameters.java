package org.polyforms.parameter.support;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;
import org.springframework.util.Assert;

/**
 * {@link Parameters} to extra parameters information from {@link Method}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class ReturnValueParameters<P extends Parameter> implements Parameters<P> {
    private final ReturnParameterAware<P> parameters;
    private P[] parametersWithReturnValue;

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
    @SuppressWarnings("unchecked")
    public P[] getParameters() {
        if (parametersWithReturnValue == null) {
            final P[] originalParameters = parameters.getParameters();
            final P returnParameter = parameters.getReturnParameter();
            if (returnParameter == null) {
                parametersWithReturnValue = originalParameters;
            } else {
                parametersWithReturnValue = (P[]) Array.newInstance(originalParameters.getClass().getComponentType(),
                        originalParameters.length + 1);
                System.arraycopy(originalParameters, 0, parametersWithReturnValue, 0, originalParameters.length);
                parametersWithReturnValue[originalParameters.length] = returnParameter;
            }
        }
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
        return parameters.equals(obj);
    }
}
