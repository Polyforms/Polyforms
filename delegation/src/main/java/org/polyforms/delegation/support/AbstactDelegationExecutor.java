package org.polyforms.delegation.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.delegation.util.DefaultValue;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

/**
 * The template abstract class to implement {@link DelegationExecutor}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
abstract class AbstactDelegationExecutor implements DelegationExecutor {
    private final ConversionService conversionService;

    protected AbstactDelegationExecutor(final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * {@inheritDoc}
     */
    public final Object execute(final Object originalTarget, final Delegation delegation, final Object[] arguments)
            throws Throwable {
        final Object target = getTarget(delegation, arguments);
        final Object[] convertedAguments = convertParameters(target, delegation.getDelegatee(),
                tailorArguments(arguments));

        try {
            final Object returnValue = delegation.getDelegatee().invoke(target, convertedAguments);
            return convertReturnValue(returnValue, originalTarget, delegation.getDelegator());
        } catch (final InvocationTargetException e) {
            throw convertException(e.getCause(), delegation); // NOPMD
        }
    }

    protected abstract Object getTarget(final Delegation delegation, Object[] arguments);

    protected Object[] tailorArguments(final Object[] arguments) {
        return arguments;
    }

    private Object[] convertParameters(final Object target, final Method delegatee, final Object[] arguments) {
        final Class<?>[] parameterTypes = delegatee.getParameterTypes();
        if (parameterTypes.length > arguments.length) {
            throw new IllegalArgumentException(
                    "The arguments passed to delegator is less than parameters required by delegatee.");
        }
        final Object[] targets = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> targetClass = GenericTypeResolver.resolveParameterType(new MethodParameter(delegatee, i),
                    target.getClass());
            targets[i] = conversionService.convert(arguments[i], targetClass);
        }
        return targets;
    }

    private Object convertReturnValue(final Object returnValue, final Object target, final Method delegator) {
        final Class<?> returnType = delegator.getReturnType();

        if (returnValue == null) {
            return DefaultValue.get(returnType);
        }

        if (returnType == void.class || returnType == Void.class) {
            return returnValue;
        }
        final Class<?> targetClass = GenericTypeResolver.resolveReturnType(delegator, target.getClass());
        return conversionService.convert(returnValue, targetClass);
    }

    private Throwable convertException(final Throwable exception, final Delegation delegation) {
        final Class<?> exceptionType = getExceptionType(exception, delegation);
        if (exceptionType == null) {
            return exception;
        }

        return (Throwable) conversionService.convert(exception, exceptionType);
    }

    private Class<?> getExceptionType(final Throwable exception, final Delegation delegation) {
        final Class<?>[] exceptions = delegation.getDelegator().getExceptionTypes();
        final Class<?> exceptionType = getExceptionTypeByPosition(exception, delegation, exceptions);
        if (exceptionType != null) {
            return exceptionType;
        }
        return getExceptionTypeByName(exception, exceptions);
    }

    private Class<?> getExceptionTypeByPosition(final Throwable exception, final Delegation delegation,
            final Class<?>[] exceptions) {
        final Class<?>[] targetExceptions = delegation.getDelegatee().getExceptionTypes();
        for (int i = 0; i < targetExceptions.length; i++) {
            if (targetExceptions[i] == exception.getClass() && exceptions.length > i) {
                return exceptions[i];
            }
        }
        return null;
    }

    private Class<?> getExceptionTypeByName(final Throwable exception, final Class<?>[] exceptions) {
        for (final Class<?> targetException : exceptions) {
            if (targetException.getSimpleName().equals(exception.getClass().getSimpleName())) {
                return targetException;
            }
        }

        return null;
    }
}
