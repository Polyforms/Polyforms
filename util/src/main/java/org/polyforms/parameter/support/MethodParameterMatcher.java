package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.polyforms.parameter.ParameterMatcher;
import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.SourceParameters;
import org.polyforms.parameter.annotation.Provider;
import org.polyforms.parameter.provider.ArgumentAt;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class MethodParameterMatcher implements ParameterMatcher<MethodParameter, MethodParameter> {
    public ArgumentProvider[] match(final Parameters<MethodParameter> sourceParameters,
            final Parameters<MethodParameter> targetParameters) {
        final GenericSourceParameters sourceParametersWrapper = new GenericSourceParameters(sourceParameters);
        final MethodParameter[] parameters = targetParameters.getParameters();

        final ArgumentProvider[] argumentProviders = new ArgumentProvider[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            argumentProviders[i] = getArgumentProvider(sourceParametersWrapper, parameters[i]);
        }

        return argumentProviders;
    }

    private ArgumentProvider getArgumentProvider(final SourceParameters sourceParameters,
            final MethodParameter parameter) {
        final Annotation annotation = parameter.getAnnotation();
        if (annotation != null) {
            return buildProviderFromAnnotation(annotation);
        }

        return new ArgumentAt(sourceParameters.match(parameter).getIndex());
    }

    private ArgumentProvider buildProviderFromAnnotation(final Annotation annotation) {
        final Provider provider = annotation.getClass().getAnnotation(Provider.class);
        final Class<?> providerClass = provider.value();
        final Object argument = AnnotationUtils.getDefaultValue(annotation);
        final Constructor<?> constructor = ClassUtils.getConstructorIfAvailable(providerClass,
                new Class<?>[] { argument.getClass() });
        try {
            return (ArgumentProvider) constructor.newInstance(argument);
        } catch (final Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }

        throw new IllegalStateException("Should never get here");
    }
}
