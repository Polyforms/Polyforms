package org.polyforms.util;

import java.lang.reflect.Method;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConversionService;

/**
 * Utility class for converting of method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class ConversionUtils {
    protected ConversionUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Convert arguments for provided method.
     */
    public static Object[] convertArguments(final ConversionService conversionService, final Class<?> targetClass,
            final Method method, Object[] arguments) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] convertedArguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> genericType = GenericTypeResolver.resolveParameterType(
                    new org.springframework.core.MethodParameter(method, i), targetClass);
            final Object argument = arguments[i];
            if (genericType.isInstance(argument)) {
                convertedArguments[i] = argument;
            } else {
                convertedArguments[i] = conversionService.convert(argument, genericType);
            }
        }
        return convertedArguments;
    }

    /**
     * Convert return value for provided method.
     */
    public static Object convertReturnValue(final ConversionService conversionService, final Class<?> targetClass,
            final Method method, final Object returnValue) {
        final Class<?> returnType = method.getReturnType();

        if (returnType == void.class || returnValue == null) {
            return DefaultValue.get(returnType);
        }

        final Class<?> genericReturnType = GenericTypeResolver.resolveReturnType(method, targetClass);

        return genericReturnType.isInstance(returnValue) ? returnValue : conversionService.convert(returnValue,
                genericReturnType);
    }
}
