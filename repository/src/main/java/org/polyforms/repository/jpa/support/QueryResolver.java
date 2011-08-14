package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

/**
 * Strategy of resolving name of named query for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
interface QueryResolver {
    /**
     * Resolve names of named query for specific method.
     * 
     * @param targetClass class of entity
     * @param method to be executed by Executor
     * 
     * @return name which might be used as query name
     */
    String getQuery(Class<?> entityClass, Method method);
}
