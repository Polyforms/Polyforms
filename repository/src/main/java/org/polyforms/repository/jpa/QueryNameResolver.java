package org.polyforms.repository.jpa;

import java.lang.reflect.Method;

/**
 * Strategy of resolving name of named query for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface QueryNameResolver {
    /**
     * Resolve names of named query for specific method.
     * 
     * @param method to be executed by Executor
     * 
     * @return name which might be used as query name
     */
    String getQueryName(Method method);
}
