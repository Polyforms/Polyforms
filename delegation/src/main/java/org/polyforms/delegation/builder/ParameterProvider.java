package org.polyforms.delegation.builder;

public interface ParameterProvider<P> {
    void validate(Class<?>[] parameterTypes);

    P get(Object[] arguments);
}
