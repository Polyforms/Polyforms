package org.polyforms.repository.jpa;

import java.lang.reflect.Method;

import javax.persistence.Query;

import org.polyforms.repository.spi.Executor;

/**
 * Strategy of build query for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface QueryBuilder {
    /**
     * Build {@link Query} from specified method.
     * 
     * @param entityClass class of entity
     * @param method to create a query
     * @return Query built from method.
     * 
     * @throws IllegalArgumentException if cannot build
     */
    Query build(Executor executor, Class<?> entityClass, Method method);
}
