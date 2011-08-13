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
import org.springframework.util.ClassUtils;

public final class DefaultDelegationBuilder implements DelegationBuilder {
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

    public void map(final Class<? extends Throwable> sourceType, final Class<? extends Throwable> targetType) {
        exceptionTypeMap.put(targetType, sourceType);
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

    @SuppressWarnings("unchecked")
    public <T> T delegate() {
        if (delegatorMethod != null) {
            delegation = newDelegation(delegatorMethod);
            registerDelegation(delegation);
            delegatorMethod = null;
            parameterProviders = new ArrayList<ParameterProvider<?>>();
            if (delegateeType == null) {
                return (T) delegateeProxyFactory.getProxy(delegation.getDelegateeType());
            }
        } else {
            registerAllAbstractMethods();
        }

        return null;
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
        if (parameterTypes.length == 0) {
            throw new IllegalArgumentException("The delegatee method must have at lease one parameter.");
        }

        return parameterTypes[0];
    }

    private void registerDelegation(final SimpleDelegation delegation) {
        delegations.add(delegation);
    }

    public void parameter(final ParameterProvider<?> parameterProvider) {
        if (parameterProviders == null) {
            throw new IllegalArgumentException("the parameter must be invoked after delegate.");
        }
        parameterProviders.add(parameterProvider);
    }

    private final class DelegatorMethodVisitor implements MethodVisitor {
        public void visit(final Method method) {
            if (delegatorMethod != null) {
                throw new IllegalArgumentException("Invoke source.xxx twice");
            }
            delegatorMethod = method;
        }
    }

    private final class DelegateeMethodVisitor implements MethodVisitor {
        public void visit(final Method method) {
            if (delegation.getDelegateeMethod() != null) {
                throw new IllegalArgumentException("The delegatee method has been set.");
            }
            delegation.setDelegateeMethod(method);

            if (parameterProviders.isEmpty()) {
                parameterProviders = matchParameterTypes(delegation.getDelegatorMethod().getParameterTypes(),
                        method.getParameterTypes());
            }

            if (!parameterProviders.isEmpty()) {
                setParameterProviders();
            }

            parameterProviders = null;
        }

        @SuppressWarnings("unchecked")
        private List<ParameterProvider<?>> matchParameterTypes(final Class<?>[] delegatorParameterTypes,
                final Class<?>[] delegateeParameterTypes) {
            if (delegatorParameterTypes.length == 0 || delegateeParameterTypes.length == 0) {
                return Collections.EMPTY_LIST;
            }

            final Map<Class<?>, Integer> delegatorParameterTypeMap = createParameterTypeMap(delegatorParameterTypes);
            if (delegatorParameterTypeMap.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            final List<ParameterProvider<?>> resolvedParameterProviders = new ArrayList<ParameterProvider<?>>();
            for (final Class<?> delegateeParameter : delegateeParameterTypes) {
                final ParameterProvider<?> parameterProvider = findMatchedParameter(
                        ClassUtils.resolvePrimitiveIfNecessary(delegateeParameter), delegatorParameterTypeMap);
                if (parameterProvider == null) {
                    return Collections.EMPTY_LIST;
                }
                resolvedParameterProviders.add(parameterProvider);
            }

            return resolvedParameterProviders;
        }

        @SuppressWarnings("rawtypes")
        private ParameterProvider<?> findMatchedParameter(final Class<?> delegateeParameterType,
                final Map<Class<?>, Integer> delegatorParameterTypeMap) {
            if (delegatorParameterTypeMap.containsKey(delegateeParameterType)) {
                return new At(delegatorParameterTypeMap.remove(delegateeParameterType));
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        private Map<Class<?>, Integer> createParameterTypeMap(final Class<?>[] parameterTypes) {
            final Map<Class<?>, Integer> parameterTypeMap = new HashMap<Class<?>, Integer>();
            for (int i = 0; i < parameterTypes.length; i++) {
                final Class<?> parameterType = parameterTypes[i];
                if (parameterTypeMap.containsKey(parameterType)) {
                    return Collections.EMPTY_MAP;
                }
                parameterTypeMap.put(ClassUtils.resolvePrimitiveIfNecessary(parameterType), i);
            }
            return parameterTypeMap;
        }

        private void setParameterProviders() {
            if (parameterProviders.size() != delegation.getDelegateeMethod().getParameterTypes().length) {
                throw new IllegalArgumentException("Unmatched parameter providers and parameter types of method.");
            }
            for (final ParameterProvider<?> parameterProvider : parameterProviders) {
                parameterProvider.validate(delegation.getDelegatorMethod().getParameterTypes());
                delegation.addParameterProvider(parameterProvider);
            }
        }
    }

    public void registerDelegations() {
        if (delegations.isEmpty()) {
            registerAllAbstractMethods();
        }

        for (final SimpleDelegation newDelegation : delegations) {
            if (newDelegation.getDelegateeMethod() == null) {
                final Method delegateeMethod = MethodUtils.findMostSpecificMethod(newDelegation.getDelegateeType(),
                        newDelegation.getDelegatorMethod().getName());
                if (delegateeMethod == null) {
                    throw new IllegalArgumentException("The mathod with same name cannot find in delegatee type");
                }
                newDelegation.setDelegateeMethod(delegateeMethod);
            }
            newDelegation.setExceptionTypeMap(exceptionTypeMap);
            delegationRegistry.register(newDelegation);
        }
        delegatorType = null;
        delegations.clear();
        exceptionTypeMap = null;
        resetDelegatee();
    }
}
