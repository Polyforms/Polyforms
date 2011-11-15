package org.polyforms.parameter.support;

import java.util.Map;
import java.util.WeakHashMap;

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
    private final Map<ParametersPair, ArgumentProvider[]> argumentProvidersCache = new WeakHashMap<ParametersPair, ArgumentProvider[]>();

    /**
     * {@inheritDoc}
     */
    public ArgumentProvider[] match(final Parameters<S> sourceParameters, final Parameters<T> targetParameters) {
        final ParametersPair parametersPair = new ParametersPair(sourceParameters, targetParameters);
        if (!argumentProvidersCache.containsKey(parametersPair)) {
            final SourceParameters sourceParametersWrapper = new SourceParameters(sourceParameters);
            final T[] parameters = targetParameters.getParameters();

            final ArgumentProvider[] argumentProviders = new ArgumentProvider[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                argumentProviders[i] = getArgumentProvider(sourceParametersWrapper, parameters[i]);
            }
            argumentProvidersCache.put(parametersPair, argumentProviders);
        }
        return argumentProvidersCache.get(parametersPair);
    }

    protected abstract ArgumentProvider getArgumentProvider(SourceParameters sourceParameters, T parameter);

    protected static class ParametersPair {
        private final Parameters<?> sourceParameters;
        private final Parameters<?> targetParameters;

        protected ParametersPair(final Parameters<?> sourceParameters, final Parameters<?> targetParameters) {
            this.sourceParameters = sourceParameters;
            this.targetParameters = targetParameters;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + sourceParameters.hashCode();
            result = prime * result + targetParameters.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof ParametersPair)) {
                return false;
            }

            final ParametersPair other = (ParametersPair) obj;
            return sourceParameters.equals(other.sourceParameters) && targetParameters.equals(other.targetParameters);
        }
    }
}
