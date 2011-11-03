package org.polyforms.delegation.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.builder.BeanContainer;
import org.polyforms.delegation.builder.Delegation;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.polyforms.parameter.support.MethodParameterMatcher;
import org.polyforms.util.DefaultValue;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * The template abstract class to implement {@link DelegationExecutor}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
class DelegationExecutor {
    private final MethodParameterMatcher parameterMatcher = new MethodParameterMatcher();
    private final BeanContainer beanContainer;
    private final ConversionService conversionService;

    @Inject
    protected DelegationExecutor(final BeanContainer beanContainer, final ConversionService conversionService) {
        this.conversionService = conversionService;
        this.beanContainer = beanContainer;
    }

    protected Object execute(final Delegation delegation, final Object... arguments) throws Throwable {
        final Class<?> delegateeType = delegation.getDelegateeType();
        final Method delegateeMethod = delegation.getDelegateeMethod();
        final String delegateeName = delegation.getDelegateeName();

        final boolean beanDelegation = isBeanDelegation(delegateeName, delegateeType);
        final Object target = getTarget(beanDelegation, delegateeName, delegateeType, arguments);
        final List<ArgumentProvider> argumentProviders = getArgumentProviders(delegation, beanDelegation);

        final Object[] tailoredArguments = getArguments(argumentProviders, arguments);
        Assert.isTrue(arguments.length >= delegateeMethod.getParameterTypes().length,
                "The arguments passed to are less than parameters required by method.");

        final Object convertedTarget = conversionService.convert(target, delegateeType);
        final Object[] convertedAguments = convertArguments(tailoredArguments, convertedTarget.getClass(),
                delegateeMethod);

        try {
            final Object returnValue = delegateeMethod.invoke(convertedTarget, convertedAguments);
            return convertReturnValue(returnValue, delegation.getDelegatorType(), delegation.getDelegatorMethod());
        } catch (final InvocationTargetException e) {
            final Throwable exception = e.getTargetException();
            final Class<? extends Throwable> delegatorExceptionType = getDelegatorExceptionType(delegation,
                    exception.getClass());

            if (delegatorExceptionType == null) {
                throw exception;
            }
            throw conversionService.convert(exception, delegatorExceptionType); // NOPMD
        }
    }

    private List<ArgumentProvider> getArgumentProviders(final Delegation delegation, final boolean beanDelegation) {
        List<ArgumentProvider> argumentProviders = delegation.getArgumentProviders();
        if (argumentProviders.isEmpty()) {
            argumentProviders = Arrays.asList(parameterMatcher.match(delegation.getDelegatorType(),
                    delegation.getDelegatorMethod(), delegation.getDelegateeType(), delegation.getDelegateeMethod(),
                    beanDelegation ? 0 : 1));
        }
        return argumentProviders;
    }

    protected Object getTarget(final boolean beanDelegation, final String delegateeName, final Class<?> delegateeType,
            final Object[] arguments) {
        if (beanDelegation) {
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
        Assert.notEmpty(arguments, "There is no auguments. ");
        final Object argument = arguments[0];
        Assert.notNull(argument, "The first argument of invocation must not be null.");
        return argument;
    }

    private Object[] getArguments(final List<ArgumentProvider> argumentProviders, final Object[] arguments) {
        final Object[] tailoredArguments = new Object[argumentProviders.size()];
        for (int i = 0; i < argumentProviders.size(); i++) {
            tailoredArguments[i] = argumentProviders.get(i).get(arguments);
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

    private Class<? extends Throwable> getDelegatorExceptionType(final Delegation delegation,
            final Class<? extends Throwable> delegateeExceptionType) {
        Class<? extends Throwable> delegatorExceptionType = delegation.getExceptionType(delegateeExceptionType);
        if (delegatorExceptionType == null) {
            delegatorExceptionType = getExceptionTypeByName(delegateeExceptionType, delegation.getDelegatorMethod());
        }
        return delegatorExceptionType;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Throwable> getExceptionTypeByName(final Class<? extends Throwable> delegateeExceptionType,
            final Method delegatorMethod) {
        for (final Class<?> delegatorExceptionType : delegatorMethod.getExceptionTypes()) {
            if (delegatorExceptionType.getSimpleName().equals(delegateeExceptionType.getSimpleName())) {
                return (Class<? extends Throwable>) delegatorExceptionType;
            }
        }

        return null;
    }
}
