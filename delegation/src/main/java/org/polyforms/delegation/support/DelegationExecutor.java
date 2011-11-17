package org.polyforms.delegation.support;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.builder.BeanContainer;
import org.polyforms.delegation.builder.Delegation;
import org.polyforms.parameter.ArgumentProvider;
import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.ParameterMatcher;
import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.support.MethodParameter;
import org.polyforms.parameter.support.MethodParameterMatcher;
import org.polyforms.parameter.support.MethodParameters;
import org.polyforms.util.ConversionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DelegationExecutor.class);
    private final ParameterMatcher<MethodParameter, MethodParameter> parameterMatcher = new MethodParameterMatcher();
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
        if (beanDelegation) {
            LOGGER.debug("Is bean delegation to {}.", delegateeMethod);
        }
        final Object target = getTarget(beanDelegation, delegateeName, delegateeType, arguments);
        LOGGER.trace("Target of delegation to {} is {}.", delegateeMethod, target);
        final Object[] matchedArguments = getArguments(delegation, beanDelegation, arguments);
        LOGGER.trace("Parameters of delegation to {} is {}.", delegateeMethod, Arrays.toString(matchedArguments));
        Assert.isTrue(arguments.length >= delegateeMethod.getParameterTypes().length,
                "The arguments passed to are less than parameters required by method.");

        final Object convertedTarget = conversionService.convert(target, delegateeType);
        LOGGER.debug("Converted target of delegation to {} is {}.", delegateeMethod, convertedTarget);
        final Object[] convertedAguments = ConversionUtils.convertArguments(conversionService,
                convertedTarget.getClass(), delegateeMethod, matchedArguments);
        LOGGER.debug("Converted parameters of delegation to {} is {}.", delegateeMethod,
                Arrays.toString(convertedAguments));

        try {
            final Object returnValue = delegateeMethod.invoke(convertedTarget, convertedAguments);
            LOGGER.trace("Return value of delegation to {} is {}.", delegateeMethod, returnValue);
            final Object convertedReturnValue = ConversionUtils.convertReturnValue(conversionService,
                    delegation.getDelegatorType(), delegation.getDelegatorMethod(), returnValue);
            LOGGER.debug("Converted return value of delegation to {} is {}.", delegateeMethod, convertedReturnValue);
            return convertedReturnValue;
        } catch (final InvocationTargetException e) {
            LOGGER.trace("Exception of delegation to {} is {}.", delegateeMethod, e);
            final Throwable exception = e.getTargetException();
            final Class<? extends Throwable> delegatorExceptionType = getDelegatorExceptionType(delegation,
                    exception.getClass());

            if (delegatorExceptionType == null) {
                throw exception;
            }
            final Throwable ce = conversionService.convert(exception, delegatorExceptionType);
            LOGGER.trace("Converted exception of delegation to {} is {}.", delegateeMethod, ce);
            throw ce; // NOPMD
        }
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

    private Object[] getArguments(final Delegation delegation, final boolean beanDelegation, final Object[] arguments) {
        final ArgumentProvider[] argumentProviders = getArgumentProviders(delegation, beanDelegation);
        final Object[] tailoredArguments = new Object[argumentProviders.length];
        for (int i = 0; i < argumentProviders.length; i++) {
            tailoredArguments[i] = argumentProviders[i].get(arguments);
        }
        return tailoredArguments;
    }

    private ArgumentProvider[] getArgumentProviders(final Delegation delegation, final boolean beanDelegation) {
        ArgumentProvider[] argumentProviders = delegation.getArgumentProviders();
        if (argumentProviders.length == 0) {
            argumentProviders = match(delegation.getDelegatorType(), delegation.getDelegatorMethod(),
                    delegation.getDelegateeType(), delegation.getDelegateeMethod(), beanDelegation ? 0 : 1);
        }
        return argumentProviders;
    }

    private ArgumentProvider[] match(final Class<?> sourceClass, final Method sourceMethod, final Class<?> targetClass,
            final Method targetMethod, final int offset) {
        final MethodParameters sourceParameters = new MethodParameters(sourceClass, sourceMethod);
        sourceParameters.applyAnnotation();
        final MethodParameters targetParameters = new MethodParameters(targetClass, targetMethod);
        for (final MethodParameter parameter : targetParameters.getParameters()) {
            parameter.setIndex(parameter.getIndex() + offset);
        }
        return parameterMatcher.match(new TailorableParameters<MethodParameter>(sourceParameters, offset),
                targetParameters);
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

    private static final class TailorableParameters<P extends Parameter> implements Parameters<P> {
        private final Parameters<P> parameters;
        private final int offset;

        private TailorableParameters(final Parameters<P> parameters, final int offset) {
            this.parameters = parameters;
            this.offset = offset;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public P[] getParameters() {
            final P[] originalParameters = parameters.getParameters();
            final P[] tailoredParameters = (P[]) Array.newInstance(originalParameters.getClass().getComponentType(),
                    originalParameters.length - offset);
            System.arraycopy(originalParameters, offset, tailoredParameters, 0, tailoredParameters.length);
            return tailoredParameters;
        }
    }
}
