package org.polyforms.parameter.support;

import org.polyforms.parameter.ArgumentProvider;
import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.ParameterMatcher;
import org.polyforms.parameter.Parameters;

/**
 * Abstract implementation of {@link ParameterMatcher}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public abstract class AbstractParameterMatcher<S extends Parameter, T extends Parameter> implements
        ParameterMatcher<S, T> {
    /**
     * {@inheritDoc}
     */
    public ArgumentProvider[] match(final Parameters<S> sourceParameters, final Parameters<T> targetParameters) {
        final SourceParameters sourceParametersWrapper = new SourceParameters(sourceParameters);
        final T[] parameters = targetParameters.getParameters();

        final ArgumentProvider[] argumentProviders = new ArgumentProvider[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            argumentProviders[i] = getArgumentProvider(sourceParametersWrapper, parameters[i]);
        }

        return argumentProviders;
    }

    protected abstract ArgumentProvider getArgumentProvider(SourceParameters sourceParameters, T parameter);
}
