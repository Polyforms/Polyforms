package org.polyforms.repository.jpa;

import java.lang.reflect.Method;

import javax.persistence.Query;

/**
 * Strategy of bind parameters to @{link Query} related with specified method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface QueryParameterBinder {
    /**
     * Bind parameters to specified {@link Query}.
     * 
     * @param query to bind parameters
     * @param method related specified query
     * @param arguments to bind
     */
    void bind(Query query, Method method, Object[] arguments);
}
