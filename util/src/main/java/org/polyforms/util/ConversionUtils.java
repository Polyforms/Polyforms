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
            final Object argument = arguments[i];
            convertedArguments[i] = conversionService.convert(argument, TypeDescriptor.forObject(argument),
                    createTypeDescriptor(method, i, targetClass));
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

        return conversionService.convert(returnValue, TypeDescriptor.forObject(returnValue),
                createTypeDescriptor(method, -1, targetClass));
    }

    private static TypeDescriptor createTypeDescriptor(final Method method, final int index, Class<?> targetClass) {
        final MethodParameter methodParam = new MethodParameter(method, index);
        GenericTypeResolver.resolveParameterType(methodParam, targetClass);
        return new TypeDescriptor(methodParam);
    }
}
