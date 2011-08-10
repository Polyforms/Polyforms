package org.polyforms.delegation.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.ParameterProvider;
import org.polyforms.delegation.util.DefaultValue;
import org.polyforms.di.BeanContainer;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

/**
 * The template abstract class to implement {@link DelegationExecutor}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
class DelegationExecutor {
    private final BeanContainer beanContainer;
    private final ConversionService conversionService;

    /**
     * Create an instance with {@link ConversionService} and {@link BeanContainer}.
     */
    @Inject
    public DelegationExecutor(final BeanContainer beanContainer, final ConversionService conversionService) {
        this.conversionService = conversionService;
        this.beanContainer = beanContainer;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Delegation delegation, final Object... arguments) throws Throwable {
        final Class<?> delegateeType = delegation.getDelegateeType();
        final Method delegateeMethod = delegation.getDelegateeMethod();

        final Object target = getTarget(delegation.getDelegateeName(), delegateeType, arguments);
        final Object[] tailoredArguments = getArguments(delegation.getParameterProviders(), target, arguments);
        if (arguments.length < delegateeMethod.getParameterTypes().length) {
            throw new IllegalArgumentException("The arguments passed to are less than parameters required by method.");
        }

        final Object convertedTarget = conversionService.convert(target, delegateeType);
        final Object[] convertedAguments = convertArguments(tailoredArguments, convertedTarget.getClass(),
                delegateeMethod);

        try {
            final Object returnValue = delegateeMethod.invoke(convertedTarget, convertedAguments);
            return convertReturnValue(returnValue, delegation.getDelegatorType(), delegation.getDelegatorMethod());
        } catch (final InvocationTargetException e) {
            final Throwable exception = e.getTargetException();
            final Class<?> targetExceptionType = getExceptionType(exception, delegation.getDelegatorMethod(),
                    delegateeMethod);
            if (targetExceptionType == null) {
                throw exception;
            }

            throw (Throwable) conversionService.convert(exception, targetExceptionType);
        }
    }

    protected Object getTarget(final String delegateeName, final Class<?> delegateeType, final Object[] arguments) {
        if (isBeanDelegation(delegateeName, delegateeType)) {
            return getBean(delegateeName, delegateeType);
        }

        return getFromArguments(arguments);
    }

    private boolean isBeanDelegation(final String delegateeName, final Class<?> delegateeType) {
        return StringUtils.hasText(delegateeName) || beanContainer.containsBean(delegateeType);
    }

    private Object getBean(final String delegateeName, final Class<?> delegateeType) {
        if (StringUtils.hasText(delegateeName)) {
            return beanContainer.getBean(delegateeName, delegateeType);
        }

        return beanContainer.getBean(delegateeType);
    }

    private Object getFromArguments(final Object[] arguments) {
        if (arguments.length == 0) {
            throw new IllegalArgumentException("There is no auguments. ");
        }

        final Object argument = arguments[0];

        if (argument == null) {
            throw new IllegalArgumentException("The first argument of invocation must not be null.");
        }

        return argument;
    }

    private Object[] getArguments(final List<ParameterProvider<?>> parameterProviders, final Object target,
            final Object[] arguments) {
        Object[] tailoredArguments = arguments;

        if (!parameterProviders.isEmpty()) {
            tailoredArguments = new Object[parameterProviders.size()];
            int i = 0;
            for (final ParameterProvider<?> parameterProvider : parameterProviders) {
                tailoredArguments[i++] = parameterProvider.get(arguments);
            }
        } else if (arguments.length > 0 && arguments[0] == target) {
            tailoredArguments = new Object[arguments.length - 1];
            System.arraycopy(arguments, 1, tailoredArguments, 0, arguments.length - 1);
        }

        return tailoredArguments;
    }

    private Object[] convertArguments(final Object[] arguments, final Class<?> targetClass, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] targets = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> genericType = GenericTypeResolver.resolveParameterType(new MethodParameter(method, i),
                    targetClass);
            targets[i] = conversionService.convert(arguments[i], genericType);
        }
        return targets;
    }

    private Object convertReturnValue(final Object returnValue, final Class<?> targetClass, final Method method) {
        final Class<?> returnType = method.getReturnType();

        if (returnType == void.class || returnValue == null) {
            return DefaultValue.get(returnType);
        }

        return conversionService.convert(returnValue, GenericTypeResolver.resolveReturnType(method, targetClass));
    }

    private Class<?> getExceptionType(final Throwable exception, final Method sourceMethod, final Method targetMethod) {
        Class<?> exceptionType = getExceptionTypeByPosition(exception, sourceMethod, targetMethod);
        if (exceptionType == null) {
            exceptionType = getExceptionTypeByName(exception, sourceMethod);
        }
        return exceptionType;
    }

    private Class<?> getExceptionTypeByPosition(final Throwable exception, final Method sourceMethod,
            final Method targetMethod) {
        final Class<?>[] sourceExceptionTypes = sourceMethod.getExceptionTypes();
        final Class<?>[] targetExceptionTypes = targetMethod.getExceptionTypes();
        for (int i = 0; i < targetExceptionTypes.length; i++) {
            if (targetExceptionTypes[i] == exception.getClass() && sourceExceptionTypes.length > i) {
                return sourceExceptionTypes[i];
            }
        }
        return null;
    }

    private Class<?> getExceptionTypeByName(final Throwable exception, final Method sourceMethod) {
        for (final Class<?> targetException : sourceMethod.getExceptionTypes()) {
            if (targetException.getSimpleName().equals(exception.getClass().getSimpleName())) {
                return targetException;
            }
        }

        return null;
    }
}
