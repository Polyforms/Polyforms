package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.support.ProxyFactory.MethodVisitor;
import org.polyforms.delegation.util.MethodUtils;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link DelegationBuilder}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class DefaultDelegationBuilder implements DelegationBuilder {
    private final ProxyFactory delegatorProxyFactory = new ProxyFactory(new DelegatorMethodVisitor());
    private final ProxyFactory delegateeProxyFactory = new ProxyFactory(new DelegateeMethodVisitor());
    private final DelegationRegistry delegationRegistry;
    private final Set<SimpleDelegation> delegations = new HashSet<SimpleDelegation>();
    private Map<Class<? extends Throwable>, Class<? extends Throwable>> exceptionTypeMap;
    private Class<?> delegatorType;
    private Method delegatorMethod;
    private Class<?> delegateeType;
    private String delegateeName;
    private List<ArgumentProvider> argumentProviders;
    private SimpleDelegation delegation;

    /**
     * Create an instance with {@link DelegationRegistry}.
     */
    public DefaultDelegationBuilder(final DelegationRegistry delegationRegistry) {
        this.delegationRegistry = delegationRegistry;
    }

    /**
     * {@inheritDoc}
     */
    public <S> S delegateFrom(final Class<S> delegatorType) {
        this.delegatorType = delegatorType;
        exceptionTypeMap = new HashMap<Class<? extends Throwable>, Class<? extends Throwable>>();
        return delegatorProxyFactory.getProxy(delegatorType);
    }

    /**
     * {@inheritDoc}
     */
    public void delegateTo(final Class<?> delegateeType) {
        this.delegateeType = delegateeType;
        resetDelegation();
    }

    private void resetDelegatee() {
        delegateeType = null;
        delegateeName = null;
        resetDelegation();
    }

    private void resetDelegation() {
        delegatorMethod = null;
        argumentProviders = null;
        delegation = null;
    }

    /**
     * {@inheritDoc}
     */
    public void withName(final String name) {
        delegateeName = name;
    }

    /**
     * {@inheritDoc}
     */
    public <T> T delegate() {
        Assert.notNull(delegatorType,
                "The delegatorType is null. The delegatorFrom method must be invoked before delegate method.");

        T target = null;
        if (delegatorMethod == null) {
            registerAllAbstractMethods();
        } else {
            target = this.<T> registerDelegation();
        }

        return target;
    }

    @SuppressWarnings("unchecked")
    private <T> T registerDelegation() {
        delegation = newDelegation(delegatorMethod);
        registerDelegation(delegation);
        delegatorMethod = null;
        argumentProviders = new ArrayList<ArgumentProvider>();

        return (T) delegateeProxyFactory.getProxy(delegation.getDelegateeType());
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

    /**
     * {@inheritDoc}
     */
    public void parameter(final ArgumentProvider argumentProvider) {
        Assert.notNull(argumentProviders, "the parameter must be invoked after delegate.");
        argumentProviders.add(argumentProvider);
    }

    /**
     * {@inheritDoc}
     */
    public void map(final Class<? extends Throwable> sourceType, final Class<? extends Throwable> targetType) {
        Assert.notNull(exceptionTypeMap,
                "The exceptionTypeMap is null. The delegateFrom method must be invoked before map method.");
        exceptionTypeMap.put(targetType, sourceType);
    }

    private final class DelegatorMethodVisitor extends MethodVisitor {
        @Override
        protected void visit(final Method method) {
            Assert.isNull(delegatorMethod, "Invoke source.xxx twice");
            delegatorMethod = method;
        }
    }

    private final class DelegateeMethodVisitor extends MethodVisitor {
        @Override
        protected void visit(final Method method) {
            Assert.isNull(delegation.getDelegateeMethod(), "The delegatee method has been set.");
            delegation.setDelegateeMethod(method);

            if (!argumentProviders.isEmpty()) {
                setArgumentProviders();
            }

            argumentProviders = null;
        }

        private void setArgumentProviders() {
            Assert.isTrue(argumentProviders.size() == delegation.getDelegateeMethod().getParameterTypes().length,
                    "Unmatched parameter providers and parameter types of method.");
            for (final ArgumentProvider argumentProvider : argumentProviders) {
                argumentProvider.validate(delegation.getDelegatorMethod());
                delegation.addArgumentProvider(argumentProvider);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
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
