package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.ParameterProvider;

/**
 * Implementation of {@link Delegation}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
final class SimpleDelegation implements Delegation {
    private final List<ParameterProvider<?>> parameterProviders = new ArrayList<ParameterProvider<?>>();
    private Map<Class<? extends Throwable>, Class<? extends Throwable>> exceptionTypeMap;
    private final Class<?> delegatorType;
    private final Method delegatorMethod;
    private Class<?> delegateeType;
    private String delegateeName;
    private Method delegateeMethod;

    protected SimpleDelegation(final Class<?> delegatorType, final Method delegatorMethod) {
        this.delegatorType = delegatorType;
        this.delegatorMethod = delegatorMethod;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getDelegatorType() {
        return delegatorType;
    }

    /**
     * {@inheritDoc}
     */
    public Method getDelegatorMethod() {
        return delegatorMethod;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getDelegateeType() {
        return delegateeType;
    }

    /**
     * {@inheritDoc}
     */
    public Method getDelegateeMethod() {
        return delegateeMethod;
    }

    /**
     * {@inheritDoc}
     */
    public String getDelegateeName() {
        return delegateeName;
    }

    /**
     * {@inheritDoc}
     */
    public List<ParameterProvider<?>> getParameterProviders() {
        return Collections.unmodifiableList(parameterProviders);
    }

    /**
     * {@inheritDoc}
     */
    public Class<? extends Throwable> getExceptionType(final Class<? extends Throwable> exceptionType) {
        if (exceptionTypeMap == null) {
            return null;
        }
        return exceptionTypeMap.get(exceptionType);
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

    protected void setExceptionTypeMap(
            final Map<Class<? extends Throwable>, Class<? extends Throwable>> exceptionTypeMap) {
        this.exceptionTypeMap = exceptionTypeMap;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + delegatorType.hashCode();
        result = prime * result + delegatorMethod.hashCode();
        return result;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof SimpleDelegation)) {
            return false;
        }

        final SimpleDelegation other = (SimpleDelegation) obj;
        return delegatorType == other.delegatorType && delegatorMethod.equals(other.delegatorMethod);
    }
}
