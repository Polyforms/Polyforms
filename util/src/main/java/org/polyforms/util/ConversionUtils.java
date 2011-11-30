package org.polyforms.util;

import java.lang.reflect.Method;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

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
            final Method method, final Object[] arguments) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] convertedArguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final MethodParameter methodParam = new MethodParameter(method, i);
            final Class<?> genericType = GenericTypeResolver.resolveParameterType(methodParam, targetClass);
            final Object argument = arguments[i];
            convertedArguments[i] = conversionService.convert(argument, TypeDescriptor.forObject(argument),
                    new TypeDescriptor(methodParam, genericType));
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

        return conversionService.convert(returnValue, TypeDescriptor.forObject(returnValue), new TypeDescriptor(
                new MethodParameter(method, -1), genericReturnType));
    }
}
