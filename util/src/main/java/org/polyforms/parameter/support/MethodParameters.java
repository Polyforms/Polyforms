package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.polyforms.parameter.annotation.Provider;
import org.polyforms.parameter.spi.Parameter;
import org.polyforms.parameter.spi.SourceParameters;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

class MethodParameters implements SourceParameters<MethodParameter> {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final MethodParameter[] parameters;
    private Map<String, MethodParameter> namedParameters;
    private Map<Class<?>, List<MethodParameter>> typedParameters;
    private Map<Integer, MethodParameter> indexedParameters;

    protected MethodParameters(Class<?> clazz, Method method, boolean applyAnnotation, int offset) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        parameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> type = ClassUtils.resolvePrimitiveIfNecessary(GenericTypeResolver.resolveParameterType(
                    new org.springframework.core.MethodParameter(method, i), clazz));
            MethodParameter parameter = new MethodParameter(type, i + offset);
            parameters[i] = parameter;

            if (parameterNames != null) {
                final String name = parameterNames[i];
                parameter.setName(name);
            }
        }

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameters.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
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

    public MethodParameter getMatchedParameter(Parameter parameter) {
        if (namedParameters == null) {
            namedParameters = new HashMap<String, MethodParameter>();
            for (MethodParameter sourceParameter : parameters) {
                namedParameters.put(sourceParameter.getName(), sourceParameter);
            }
        }
        MethodParameter matchedParameter = namedParameters.get(parameter.getName());

        if (typedParameters == null) {
            typedParameters = new HashMap<Class<?>, List<MethodParameter>>();
            for (MethodParameter sourceParameter : parameters) {
                Class<?> type = sourceParameter.getType();
                if (!typedParameters.containsKey(type)) {
                    typedParameters.put(type, new ArrayList<MethodParameter>());
                }
                typedParameters.get(type).add(sourceParameter);
            }
        }
        if (matchedParameter == null) {
            List<MethodParameter> matchedParameters = typedParameters.get(parameter.getType());
            if (matchedParameters != null && matchedParameters.size() == 1) {
                matchedParameter = matchedParameters.get(0);
            }
        }

        if (indexedParameters == null) {
            indexedParameters = new HashMap<Integer, MethodParameter>();
            for (MethodParameter sourceParameter : parameters) {
                indexedParameters.put(sourceParameter.getIndex(), sourceParameter);
            }
        }
        if (matchedParameter == null) {
            matchedParameter = indexedParameters.get(parameter.getIndex());
        }

        if (matchedParameter == null) {
            throw new IllegalArgumentException("Cannot find matched parameter for " + parameter);
        }

        return matchedParameter;
    }
}

class MethodParameter extends Parameter {
    private Annotation annotation;

    protected MethodParameter(Class<?> type, int index) {
        super(type, index);
    }

    protected void setAnnotation(Annotation annotation, boolean apply) {
        this.annotation = annotation;
        if (apply) {
            this.applyAnnotation(annotation);
        }
    }

    protected Annotation getAnnotation() {
        return annotation;
    }
}
