package org.polyforms.delegation.builder;

import java.lang.reflect.Method;
import java.util.List;

public interface Delegation {
    public abstract Class<?> getDelegatorType();

    public abstract Method getDelegatorMethod();

    public abstract Class<?> getDelegateeType();

    public abstract Method getDelegateeMethod();

    public abstract String getDelegateeName();

    public abstract List<ParameterProvider<?>> getParameterProviders();

    public abstract boolean hasDelegateeName();
}
