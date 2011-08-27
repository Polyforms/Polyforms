package org.polyforms.delegation.builder;

public interface DelegationBuilder {
    <S> S delegateFrom(Class<S> delegatorType);

    void delegateTo(Class<?> delegateeType);

    void withName(String name);

    <T> T delegate();

    void parameter(ParameterProvider<?> parameterProvider);

    void map(Class<? extends Throwable> sourceType, Class<? extends Throwable> targetType);

    void registerDelegations();
}
