package org.polyforms.delegation.builder;

import java.lang.reflect.Method;
import java.util.List;

public interface Delegation {
    Class<?> getDelegatorType();

    Method getDelegatorMethod();

    Class<?> getDelegateeType();

    Method getDelegateeMethod();

    String getDelegateeName();

    List<ParameterProvider<?>> getParameterProviders();

    boolean hasDelegateeName();
}
