package org.polyforms.delegation.builder;

public interface DelegationBuilder {
    <S> S from(Class<S> delegatorType);

    <T> T to(Class<T> delegateeType);

    void withName(String name);

    <T> T delegate();

    void parameter(ParameterProvider<?> parameterProvider);

    void map(Class<? extends Throwable> sourceType, Class<? extends Throwable> targetType);

    void registerDelegations();
}
