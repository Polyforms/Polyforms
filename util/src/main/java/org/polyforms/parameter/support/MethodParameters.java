package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.polyforms.parameter.annotation.Provider;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link org.polyforms.parameter.Parameters} to extra parameters information from {@link Method}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class MethodParameters implements ReturnParameterAware<MethodParameter> {
    private static final String NAME_OF_RETURN_VALUE = "returnValue";
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final Class<?> clazz;
    private final Method method;
    private final MethodParameter[] parameters;

    /**
     * Create an instance from provided method.
     */
    public MethodParameters(final Class<?> clazz, final Method method) {
        Assert.notNull(clazz);
        Assert.notNull(method);

        this.clazz = clazz;
        this.method = method;

        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        parameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final MethodParameter parameter = new MethodParameter();
            parameters[i] = parameter;

            parameter.setIndex(i);

            final Class<?> type = ClassUtils.resolvePrimitiveIfNecessary(GenericTypeResolver.resolveParameterType(
                    new org.springframework.core.MethodParameter(method, i), clazz));
            parameter.setType(type);

            if (parameterNames != null) {
                parameter.setName(parameterNames[i]);
            }

            parameter.setAnnotation(getFirstProviderAnnotation(parameterAnnotations[i]));
        }
    }

    private Annotation getFirstProviderAnnotation(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            if (AnnotationUtils.findAnnotation(annotation.getClass(), Provider.class) != null) {
                return annotation;
            }
        }

        return null;
    }

    /**
     * Apply annotations to meta data of parameters.
     */
    public void applyAnnotation() {
        for (final MethodParameter parameter : parameters) {
            parameter.applyAnnotation();
        }
    }

    /**
     * {@inheritDoc}
     */
    public MethodParameter[] getParameters() {
        final MethodParameter[] returnParameters = new MethodParameter[parameters.length];
        System.arraycopy(parameters, 0, returnParameters, 0, parameters.length);
        return returnParameters;
    }

    /**
     * {@inheritDoc}
     */
    public MethodParameter getReturnParameter() {
        final Class<?> returnType = GenericTypeResolver.resolveReturnType(method, clazz);
        if (returnType.equals(void.class)) {
            return null;
        }
        final MethodParameter returnParameter = new MethodParameter();
        returnParameter.setType(ClassUtils.resolvePrimitiveIfNecessary(returnType));
        returnParameter.setName(NAME_OF_RETURN_VALUE);
        returnParameter.setIndex(parameters.length);
        return returnParameter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + clazz.hashCode();
        result = prime * result + method.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MethodParameters)) {
            return false;
        }

        final MethodParameters other = (MethodParameters) obj;
        return clazz.equals(other.clazz) && method.equals(other.method);
    }
}
