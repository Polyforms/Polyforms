package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.ParameterProvider;
import org.polyforms.delegation.builder.ParameterProvider.At;
import org.polyforms.delegation.builder.support.Cglib2ProxyFactory.MethodVisitor;
import org.polyforms.delegation.util.MethodUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public final class DefaultDelegationBuilder implements DelegationBuilder {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ProxyFactory delegatorProxyFactory = new Cglib2ProxyFactory(new DelegatorMethodVisitor());
    private final ProxyFactory delegateeProxyFactory = new Cglib2ProxyFactory(new DelegateeMethodVisitor());
    private final DelegationRegistry delegationRegistry;
    private final Set<SimpleDelegation> delegations = new HashSet<SimpleDelegation>();
    private Map<Class<? extends Throwable>, Class<? extends Throwable>> exceptionTypeMap;
    private Class<?> delegatorType;
    private Method delegatorMethod;
    private Class<?> delegateeType;
    private String delegateeName;
    private List<ParameterProvider<?>> parameterProviders;
    private SimpleDelegation delegation;

    /**
     * Create an instance with {@link DelegationRegistry}.
     */
    public DefaultDelegationBuilder(final DelegationRegistry delegationRegistry) {
        this.delegationRegistry = delegationRegistry;
    }

    public <S> S delegateFrom(final Class<S> delegatorType) {
        this.delegatorType = delegatorType;
        exceptionTypeMap = new HashMap<Class<? extends Throwable>, Class<? extends Throwable>>();
        return delegatorProxyFactory.getProxy(delegatorType);
    }

    public <T> T delegateTo(final Class<T> delegateeType) {
        this.delegateeType = delegateeType;
        resetDelegation();
        return delegateeProxyFactory.getProxy(delegateeType);
    }

    private void resetDelegatee() {
        delegateeType = null;
        delegateeName = null;
        resetDelegation();
    }

    private void resetDelegation() {
        delegatorMethod = null;
        parameterProviders = null;
        delegation = null;
    }

    public void withName(final String name) {
        delegateeName = name;
    }

    public <T> T delegate() {
        Assert.notNull(exceptionTypeMap,
                "The delegatorType is null. The delegatorFrom method must be invoked before delegate method.");

        if (delegatorMethod == null) {
            registerAllAbstractMethods();
            return null;
        }

        return this.<T> registerDelegation();
    }

    @SuppressWarnings("unchecked")
    private <T> T registerDelegation() {
        delegation = newDelegation(delegatorMethod);
        registerDelegation(delegation);
        delegatorMethod = null;
        parameterProviders = new ArrayList<ParameterProvider<?>>();

        return delegateeType != null ? null : (T) delegateeProxyFactory.getProxy(delegation.getDelegateeType());
    }

    private void registerAllAbstractMethods() {
        for (final Method method : delegatorType.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                try {
                    final SimpleDelegation newDelegation = newDelegation(method);
                    final Method delegateeMethod = MethodUtils.findMostSpecificMethod(newDelegation.getDelegateeType(),
                            newDelegation.getDelegatorMethod().getName());
                    if (delegateeMethod != null && !contains(newDelegation)) {
                        newDelegation.setDelegateeMethod(delegateeMethod);
                        registerDelegation(newDelegation);
                    }
                } catch (final IllegalArgumentException e) {
                    // IGNORE if the delegatee type cannot be resolved from delegator method.
                }
            }
        }
    }

    private boolean contains(final Delegation newDelegation) {
        return delegations.contains(newDelegation)
                || delegationRegistry.contains(newDelegation.getDelegatorType(), newDelegation.getDelegatorMethod());
    }

    private SimpleDelegation newDelegation(final Method method) {
        final SimpleDelegation newDelegation = new SimpleDelegation(delegatorType, method);
        if (delegateeType != null) {
            newDelegation.setDelegateeType(delegateeType);
            newDelegation.setDelegateeName(delegateeName);
        } else {
            newDelegation.setDelegateeType(getTypeOfFirstParameter(method));
        }
        return newDelegation;
    }

    private Class<?> getTypeOfFirstParameter(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        Assert.notEmpty(parameterTypes, "The delegatee method must have at lease one parameter.");

        return parameterTypes[0];
    }

    private void registerDelegation(final SimpleDelegation delegation) {
        delegations.add(delegation);
    }

    public void parameter(final ParameterProvider<?> parameterProvider) {
        Assert.notNull(parameterProviders, "the parameter must be invoked after delegate.");
        parameterProviders.add(parameterProvider);
    }

    public void map(final Class<? extends Throwable> sourceType, final Class<? extends Throwable> targetType) {
        Assert.notNull(exceptionTypeMap,
                "The exceptionTypeMap is null. The delegateFrom method must be invoked before map method.");
        exceptionTypeMap.put(targetType, sourceType);
    }

    private final class DelegatorMethodVisitor implements MethodVisitor {
        public void visit(final Method method) {
            Assert.isNull(delegatorMethod, "Invoke source.xxx twice");
            delegatorMethod = method;
        }
    }

    private final class DelegateeMethodVisitor implements MethodVisitor {
        public void visit(final Method method) {
            Assert.isNull(delegation.getDelegateeMethod(), "The delegatee method has been set.");
            delegation.setDelegateeMethod(method);
            if (parameterProviders.isEmpty()) {
                parameterProviders = this.<String> matchParameters(
                        parameterNameDiscoverer.getParameterNames(delegation.getDelegatorMethod()),
                        parameterNameDiscoverer.getParameterNames(delegation.getDelegateeMethod()));
            }
            if (parameterProviders.isEmpty()) {
                parameterProviders = this.<Class<?>> matchParameters(
                        resolveGenericTypeIfNecessary(delegation.getDelegatorType(), delegation.getDelegatorMethod()),
                        resolveGenericTypeIfNecessary(delegation.getDelegateeType(), method));
            }

            if (!parameterProviders.isEmpty()) {
                setParameterProviders();
            }

            parameterProviders = null;
        }

        private Class<?>[] resolveGenericTypeIfNecessary(final Class<?> targetClass, final Method method) {
            final Class<?>[] genericTypes = new Class<?>[method.getParameterTypes().length];
            for (int i = 0; i < genericTypes.length; i++) {
                genericTypes[i] = ClassUtils.resolvePrimitiveIfNecessary(GenericTypeResolver.resolveParameterType(
                        new MethodParameter(method, i), targetClass));
            }
            return genericTypes;
        }

        @SuppressWarnings("unchecked")
        private <T> List<ParameterProvider<?>> matchParameters(final T[] delegatorParameters,
                final T[] delegateeParameters) {
            if (isEmpty(delegatorParameters) || isEmpty(delegateeParameters)) {
                return Collections.EMPTY_LIST;
            }

            final Map<T, Integer> delegatorParameterMap = this.<T> createParameterMap(delegatorParameters);
            if (delegatorParameterMap.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            final List<ParameterProvider<?>> resolvedParameterProviders = new ArrayList<ParameterProvider<?>>();
            for (final T delegateeParameter : delegateeParameters) {
                final ParameterProvider<?> parameterProvider = findMatchedParameter(delegateeParameter,
                        delegatorParameterMap);
                if (parameterProvider == null) {
                    return Collections.EMPTY_LIST;
                }
                resolvedParameterProviders.add(parameterProvider);
            }

            return resolvedParameterProviders;
        }

        private <T> boolean isEmpty(final T[] delegatorParameters) {
            return delegatorParameters == null || delegatorParameters.length == 0;
        }

        @SuppressWarnings("rawtypes")
        private <T> ParameterProvider<?> findMatchedParameter(final T delegateeParameter,
                final Map<T, Integer> delegatorParameterMap) {
            if (delegatorParameterMap.containsKey(delegateeParameter)) {
                return new At(delegatorParameterMap.remove(delegateeParameter));
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        private <T> Map<T, Integer> createParameterMap(final T[] parameters) {
            final Map<T, Integer> parameterTypeMap = new HashMap<T, Integer>();
            for (int i = 0; i < parameters.length; i++) {
                final T parameterType = parameters[i];
                if (parameterTypeMap.containsKey(parameterType)) {
                    return Collections.EMPTY_MAP;
                }
                parameterTypeMap.put(parameterType, i);
            }
            return parameterTypeMap;
        }

        private void setParameterProviders() {
            Assert.isTrue(parameterProviders.size() == delegation.getDelegateeMethod().getParameterTypes().length,
                    "Unmatched parameter providers and parameter types of method.");
            for (final ParameterProvider<?> parameterProvider : parameterProviders) {
                parameterProvider.validate(delegation.getDelegatorMethod().getParameterTypes());
                delegation.addParameterProvider(parameterProvider);
            }
        }
    }

    public void registerDelegations() {
        if (delegatorType == null) {
            return;
        }

        if (delegations.isEmpty()) {
            registerAllAbstractMethods();
        }

        registerDelegationsToRegistry();
        resetDelegator();
    }

    private void registerDelegationsToRegistry() {
        for (final SimpleDelegation newDelegation : delegations) {
            if (newDelegation.getDelegateeMethod() == null) {
                final Method delegateeMethod = MethodUtils.findMostSpecificMethod(newDelegation.getDelegateeType(),
                        newDelegation.getDelegatorMethod().getName());
                Assert.notNull(delegateeMethod, "The mathod with same name cannot find in delegatee type");
                newDelegation.setDelegateeMethod(delegateeMethod);
            }
            newDelegation.setExceptionTypeMap(exceptionTypeMap);
            delegationRegistry.register(newDelegation);
        }
    }

    private void resetDelegator() {
        delegatorType = null;
        delegations.clear();
        exceptionTypeMap = null;
        resetDelegatee();
    }
}
