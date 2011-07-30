package org.polyforms.delegation.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.delegation.util.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder to bind delegator and delegatee methods as a {@link Delegation}.
 * 
 * {@link #delegate(Class)} method {@link #to(Class)} is a shortcut to register all abstract methods in delegator class
 * to delegatee class. For Example:
 * 
 * <code> 
 *     delegate(Delegator.class, "length").to(String.class);
 *     delegate(Delegator.class).to(Delegatee.class); 
 *     delegate(Delegator.class, "name").to(String.class, "toString"); 
 *     delegate(Delegator.class, "name").to(String.class, "length"); 
 * </code>
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class DelegationBuilderFactory {
    private final Logger LOGGER = LoggerFactory.getLogger(DelegationBuilderFactory.class);
    private final DelegationRegistry delegationRegistry;

    /**
     * Create an instance with {@link DelegationRegistry}.
     */
    public DelegationBuilderFactory(final DelegationRegistry delegationRegistry) {
        this.delegationRegistry = delegationRegistry;
    }

    /**
     * Delegate all abstract methods in class.
     * 
     * @param delegator the delegator class
     * @return delegation builder of class
     */
    public DelegationBuilder<Class<?>> delegate(final Class<?> delegator) {
        return new ClassDelegationBuilder(delegator);
    }

    /**
     * Delegate a method.
     * 
     * @param delegator the delegator method
     * @return delegation builder of method
     */
    public DelegationBuilder<Method> delegate(final Method delegator) {
        return new MethodDelegationBuilder(delegator);
    }

    /**
     * Delegate a method.
     * 
     * @param clazz method declaring class
     * @param methodName method name
     * @param parameterTypes parameter types of method
     * @return delegation builder of method
     */
    public DelegationBuilder<Method> delegate(final Class<?> clazz, final String methodName,
            final Class<?>... parameterTypes) {
        return delegate(findMethod(clazz, methodName, parameterTypes));
    }

    private Method findMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
        try {
            return MethodUtils.findMostSpecificMethod(clazz, methodName, parameterTypes);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot find method " + e.getMessage());
        }
    }

    /**
     * Delegation builder of class which registers delegations for all abstract methods in specific class.
     * 
     */
    public final class ClassDelegationBuilder extends AbstractDelegationBuilder<Class<?>> {
        private ClassDelegationBuilder(final Class<?> delegator) {
            super(delegator);
            abstractClassOnly(delegator);
        }

        /**
         * {@inheritDoc}
         */
        public ClassDelegationBuilder to(final Class<?> delegatee) {
            register(delegatee);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void register(final Class<?> delegator, final Class<?> delegatee) {
            logRegistration(delegator, delegatee);
            for (final Method method : delegator.getMethods()) {
                if (Modifier.isAbstract(method.getModifiers()) && !delegationRegistry.contains(method)) {
                    try {
                        final Method targetMethod = MethodUtils.findMostSpecificMethod(delegatee, method.getName());
                        logRegistration(delegator, delegatee);
                        registerDelegation(method, targetMethod);
                    } catch (final NoSuchMethodException e) {
                        // IGNORE if the method with same name can not be found in delegatee
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public DelegationBuilder<Class<?>> to(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Delegation builder of method which registers delegation for specific method.
     * 
     */
    public final class MethodDelegationBuilder extends AbstractDelegationBuilder<Method> {
        private MethodDelegationBuilder(final Method delegator) {
            super(delegator);
            abstractClassOnly(delegator.getDeclaringClass());
        }

        /**
         * {@inheritDoc}
         */
        public MethodDelegationBuilder to(final Method delegatee) {
            register(delegatee);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public MethodDelegationBuilder to(final Class<?> clazz, final String methodName,
                final Class<?>... parameterTypes) {
            return to(findMethod(clazz, methodName, parameterTypes));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void register(final Method delegator, final Method delegatee) {
            logRegistration(delegator, delegatee);
            registerDelegation(delegator, delegatee);
        }
    }

    protected abstract class AbstractDelegationBuilder<T> implements DelegationBuilder<T> {
        private final Set<Object> REGISTERED_DELEGATORS = new HashSet<Object>();
        private final List<Delegation> resigteredDelegations = new ArrayList<Delegation>();
        private final T delegator;

        protected AbstractDelegationBuilder(final T delegator) {
            this.delegator = delegator;
        }

        /**
         * {@inheritDoc}
         */
        public final void withName(final String name) {
            if (resigteredDelegations.isEmpty()) {
                throw new IllegalArgumentException("Method withName only can be invoked after method to.");
            }
            for (final Delegation delegation : resigteredDelegations) {
                delegation.setName(name);
            }
        }

        protected final void abstractClassOnly(final Class<?> delegator) {
            if (!Modifier.isAbstract(delegator.getModifiers())) {
                throw new IllegalArgumentException("Only interface or abstract class can be a delegator.");
            }
        }

        protected final void logRegistration(final Object delegatee, final Object delegator) {
            LOGGER.info("Register delegations from {} to {}.", delegator, delegatee);
        }

        protected final void register(final T delegatee) {
            if (REGISTERED_DELEGATORS.contains(delegator)) {
                LOGGER.warn("{} had been registed, and this registration is overriding previous one.", delegator);
            }

            try {
                register(delegator, delegatee);
            } finally {
                REGISTERED_DELEGATORS.add(delegator);
            }
        }

        protected abstract void register(final T delegator, final T delegatee);

        protected final void registerDelegation(final Method method, final Method targetMethod) {
            if (delegationRegistry.contains(method)) {
                LOGGER.warn("Delegatee {} of {} is overrided by new delegatee {}.", new Object[] {
                        delegationRegistry.get(method).getDelegatee(), method, targetMethod });
            }
            final Delegation delegation = new Delegation(method, targetMethod);
            delegationRegistry.register(delegation);
            resigteredDelegations.add(delegation);
        }

        protected final T getDelegator() {
            return delegator;
        }
    }
}
