package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.polyforms.parameter.annotation.Provider;
import org.polyforms.parameter.provider.ArgumentAt;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class MethodParameterMatcher {
    public final ArgumentProvider[] match(Class<?> sourceClass, Method sourceMethod, Class<?> targetClass,
            Method targetMethod, int offset) {
        MethodParameter[] parameters = new MethodParameters(targetClass, targetMethod, false, offset).getParameters();
        ArgumentProvider[] argumentProviders = new ArgumentProvider[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            argumentProviders[i] = getArgumentProvider(new MethodParameters(sourceClass, sourceMethod, true, 0),
                    parameters[i]);
        }
        return argumentProviders;
    }

    private ArgumentProvider getArgumentProvider(MethodParameters sourceParameters, MethodParameter parameter) {
        final Annotation annotation = parameter.getAnnotation();
        if (annotation != null) {
            return buildProviderFromAnnotation(annotation);
        }

        return new ArgumentAt(sourceParameters.getMatchedParameter(parameter).getIndex());
    }

    private ArgumentProvider buildProviderFromAnnotation(Annotation annotation) {
        Provider provider = annotation.getClass().getAnnotation(Provider.class);
        Class<?> providerClass = provider.value();
        Object argument = AnnotationUtils.getDefaultValue(annotation);
        Constructor<?> constructor = ClassUtils.getConstructorIfAvailable(providerClass,
                new Class<?>[] { argument.getClass() });
        try {
            return (ArgumentProvider) constructor.newInstance(argument);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }

        throw new IllegalStateException("Should never get here");
    }
}
