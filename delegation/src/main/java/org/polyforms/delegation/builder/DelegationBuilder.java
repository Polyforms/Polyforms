package org.polyforms.delegation.builder;

public interface DelegationBuilder {
    <S> S delegateFrom(Class<S> delegatorType);

    <T> T delegateTo(Class<T> delegateeType);

    void withName(String name);

    <T> T delegate();

    void parameter(ParameterProvider<?> parameterProvider);

    void map(Class<? extends Throwable> sourceType, Class<? extends Throwable> targetType);

    void registerDelegations();
}
