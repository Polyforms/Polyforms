package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.ParameterProvider;
import org.springframework.util.StringUtils;

final class SimpleDelegation implements Delegation {
    private final Class<?> delegatorType;
    private final Method delegatorMethod;
    private Class<?> delegateeType;
    private Method delegateeMethod;
    private String delegateeName;
    private final List<ParameterProvider<?>> parameterProviders = new ArrayList<ParameterProvider<?>>();

    public SimpleDelegation(final Class<?> delegatorType, final Method delegatorMethod) {
        this.delegatorType = delegatorType;
        this.delegatorMethod = delegatorMethod;
    }

    public Class<?> getDelegatorType() {
        return delegatorType;
    }

    public Method getDelegatorMethod() {
        return delegatorMethod;
    }

    public Class<?> getDelegateeType() {
        return delegateeType;
    }

    public Method getDelegateeMethod() {
        return delegateeMethod;
    }

    public String getDelegateeName() {
        return delegateeName;
    }

    public List<ParameterProvider<?>> getParameterProviders() {
        return Collections.unmodifiableList(parameterProviders);
    }

    public boolean hasDelegateeName() {
        return StringUtils.hasText(delegateeName);
    }

    protected void addParameterProvider(final ParameterProvider<?> parameterProvider) {
        parameterProviders.add(parameterProvider);
    }

    protected void setDelegateeType(final Class<?> delegateeType) {
        this.delegateeType = delegateeType;
    }

    protected void setDelegateeMethod(final Method delegateeMethod) {
        this.delegateeMethod = delegateeMethod;
    }

    protected void setDelegateeName(final String delegateeName) {
        this.delegateeName = delegateeName;
    }
}
