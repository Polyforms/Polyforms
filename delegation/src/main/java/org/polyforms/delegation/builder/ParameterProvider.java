package org.polyforms.delegation.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Provider used to resolve argument for delegatee method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <P> type of parameter
 */
public interface ParameterProvider<P> {
    /**
     * Check whether the argument can be resolved.
     * 
     * @param parameterTypes of delegatee method
     */
    void validate(Class<?>... parameterTypes);

    /**
     * Resolve argument from invocation arguments of delegator method.
     * 
     * @param arguments of delegator method
     * @return argument used by delegatee method
     */
    P get(Object... arguments);

    /**
     * Argument Resolved by postion of arguments used to invoke delegator method.
     * 
     * @param <P> type of parameter
     */
    final class At<P> implements ParameterProvider<P> {
        private final int position;

        /**
         * Create an instance with position of parameter.
         */
        public At(final int position) {
            Assert.isTrue(position >= 0);
            this.position = position;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public P get(final Object... arguments) {
            return (P) arguments[position];
        }

        /**
         * {@inheritDoc}
         */
        public void validate(final Class<?>... parameterType) {
            Assert.isTrue(position < parameterType.length, "Parameter position " + position
                    + " must not less than parameter count " + parameterType.length + " of delegator method.");
        }
    }

    /**
     * Constant holder always returning constant value.
     * 
     * @param <P> type of parameter
     */
    final class Constant<P> implements ParameterProvider<P> {
        private final P value;

        /**
         * Create an instance with constant.
         */
        public Constant(final P value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        public P get(final Object... arguments) {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        public void validate(final Class<?>... parameterType) {
        }
    }

    /**
     * Argument Resolved by type of arguments used to invoke delegator method.
     * 
     * @param <P> type of parameter
     */
    final class TypeOf<P> implements ParameterProvider<P> {
        private final Class<?> type;

        /**
         * Create an instance with type of parameter.
         */
        public TypeOf(final Class<?> type) {
            Assert.notNull(type);
            this.type = type;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public P get(final Object... arguments) {
            for (final Object argument : arguments) {
                if (argument != null && type.isInstance(argument)) {
                    return (P) argument;
                }
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        public void validate(final Class<?>... parameterTypes) {
            final List<Class<?>> matchedParameterTypes = new ArrayList<Class<?>>();
            for (final Class<?> parameterType : parameterTypes) {
                if (type == parameterType) {
                    matchedParameterTypes.add(parameterType);
                }
            }

            Assert.isTrue(matchedParameterTypes.size() == 1, "There is one and only one parameter of type " + type
                    + " allowed in delegator method.");
        }
    }
}
