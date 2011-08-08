package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.ParameterProvider;
import org.polyforms.delegation.builder.support.Cglib2ProxyFactory.MethodVisitor;
import org.polyforms.delegation.util.MethodUtils;

public final class DefaultDelegationBuilder implements DelegationBuilder {
    private final ProxyFactory delegatorProxyFactory = new Cglib2ProxyFactory(new DelegatorMethodVisitor());
    private final ProxyFactory delegateeProxyFactory = new Cglib2ProxyFactory(new DelegateeMethodVisitor());
    private final Set<SimpleDelegation> delegations = new HashSet<SimpleDelegation>();
    private final DelegationRegistry delegationRegistry;
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

    public <S> S from(final Class<S> delegatorType) {
        this.delegatorType = delegatorType;
        return delegatorProxyFactory.getProxy(delegatorType);
    }

    public <T> T to(final Class<T> delegateeType) {
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
            if (!parameterProviders.isEmpty()) {
                setParameterProviders(method);
            }
            parameterProviders = null;
            delegation.setDelegateeMethod(method);
        }

        private void setParameterProviders(final Method method) {
            if (parameterProviders.size() != method.getParameterTypes().length) {
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
            delegationRegistry.register(newDelegation);
        }
        delegations.clear();
        resetDelegatee();
    }
}
