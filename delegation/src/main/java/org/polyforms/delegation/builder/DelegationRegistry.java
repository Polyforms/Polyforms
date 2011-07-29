package org.polyforms.delegation.builder;

import java.lang.reflect.Method;

import org.springframework.util.StringUtils;

/**
 * The registry to keep all delegation registered by {@link DelegationBuilder}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationRegistry {
    /**
     * Register a delegation. The delegation of delegator would be overrided if it existed.
     * 
     * @param delegation the delegation will be registed
     */
    void register(final Delegation delegation);

    /**
     * Get delegation of specific method.
     * 
     * @param method the delegator method
     * @return delegation of specific method
     */
    Delegation get(final Method method);

    /**
     * Check whether a delegation for specific method exists.
     * 
     * @param method the delegator method
     * @return true if there is a delegation of specific method, false if not
     */
    boolean contains(final Method method);

    /**
     * Context about a delegation.
     */
    final class Delegation {
        private final Method delegator;
        private final Method delegatee;
        private String name = "";

        /**
         * Create an instance with delegator and delegatee methods.
         */
        public Delegation(final Method delegator, final Method delegatee) {
            this.delegatee = delegatee;
            this.delegator = delegator;
        }

        /**
         * Get delegator.
         */
        public Method getDelegator() {
            return delegator;
        }

        /**
         * Get delegatee.
         */
        public Method getDelegatee() {
            return delegatee;
        }

        /**
         * Check whether the name of bean is set.
         * 
         * @return true if name is set, false if not
         */
        public boolean hasName() {
            return StringUtils.hasText(name);
        }

        /**
         * Get name.
         */
        public String getName() {
            return name;
        }

        /**
         * Set name.
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + delegatee.hashCode();
            result = prime * result + delegator.hashCode();
            result = prime * result + name.hashCode();
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            Delegation other = (Delegation) obj;

            if (!delegatee.equals(other.delegatee)) {
                return false;
            }

            if (!delegator.equals(other.delegator)) {
                return false;
            }

            if (!name.equals(other.name)) {
                return false;
            }

            return true;
        }
    }
}
