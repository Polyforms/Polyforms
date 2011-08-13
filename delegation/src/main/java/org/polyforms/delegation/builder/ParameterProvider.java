package org.polyforms.delegation.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public interface ParameterProvider<P> {
    void validate(Class<?>... parameterTypes);

    P get(Object... arguments);

    final class At<P> implements ParameterProvider<P> {
        private final int position;

        public At(final int position) {
            Assert.isTrue(position >= 0);
            this.position = position;
        }

        @SuppressWarnings("unchecked")
        public P get(final Object... arguments) {
            return (P) arguments[position];
        }

        public void validate(final Class<?>... parameterType) {
            Assert.isTrue(position < parameterType.length, "Parameter position " + position
                    + " must not less than parameter count " + parameterType.length + " of delegator method.");
        }
    }

    final class Constant<P> implements ParameterProvider<P> {
        private final P value;

        public Constant(final P value) {
            this.value = value;
        }

        public P get(final Object... arguments) {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        public void validate(final Class<?>... parameterType) {
        }
    }

    final class TypeOf<P> implements ParameterProvider<P> {
        private final Class<?> type;

        public TypeOf(final Class<?> type) {
            Assert.notNull(type);
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        public P get(final Object... arguments) {
            for (final Object argument : arguments) {
                if (argument != null && type.isInstance(argument)) {
                    return (P) argument;
                }
            }

            return null;
        }

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
