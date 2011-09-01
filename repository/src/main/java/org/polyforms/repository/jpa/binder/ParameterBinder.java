package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;

import javax.persistence.Query;

/**
 * Helper to bind arguments to JAP parameters.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
interface ParameterBinder<T> {
    /**
     * Bind arguments to JPA parameters.
     * 
     * @param query of JPA
     * @param method invoked by client
     * @param arguments passed to invocation
     */
    void bind(final Query query, final Method method, final Object[] arguments);
}
