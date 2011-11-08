package org.polyforms.parameter.support;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.ParameterMatcher;
import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.provider.ArgumentProvider;

public abstract class AbstractParameterMatcher<S extends Parameter, T extends Parameter> implements
        ParameterMatcher<S, T> {
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
