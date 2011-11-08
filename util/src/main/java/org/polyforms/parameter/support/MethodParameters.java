package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.annotation.Provider;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

public class MethodParameters implements Parameters<MethodParameter> {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final MethodParameter[] parameters;

    public MethodParameters(final Class<?> clazz, final Method method, final boolean applyAnnotation) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        parameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> type = ClassUtils.resolvePrimitiveIfNecessary(GenericTypeResolver.resolveParameterType(
                    new org.springframework.core.MethodParameter(method, i), clazz));
            final MethodParameter parameter = new MethodParameter(type, i);
            parameters[i] = parameter;

            if (parameterNames != null) {
                final String name = parameterNames[i];
                parameter.setName(name);
            }
        }

        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameters.length; i++) {
            for (final Annotation annotation : parameterAnnotations[i]) {
                if (annotation.getClass().isAnnotationPresent(Provider.class)) {
                    parameters[i].setAnnotation(annotation, applyAnnotation);
                    break;
                }
            }
        }
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

}
