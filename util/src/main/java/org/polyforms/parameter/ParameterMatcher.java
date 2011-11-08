package org.polyforms.parameter;

import org.polyforms.parameter.provider.ArgumentProvider;

public interface ParameterMatcher<S extends Parameter, T extends Parameter> {
    ArgumentProvider[] match(final Parameters<S> sourceParameters, Parameters<T> targetParameters);
}
