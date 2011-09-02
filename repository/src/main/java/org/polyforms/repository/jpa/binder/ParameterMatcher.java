package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

/**
 * Helper to match method's parameters with JPA parameters.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
abstract class ParameterMatcher<T> {
    /**
     * Match parameters of JPA with parameters of method.
     * 
     * @param method
     * @param parameters of JPA
     * @return the matched parameter map
     */
    protected abstract Map<T, Integer> match(Method method, Set<Parameter<?>> parameters);
}
