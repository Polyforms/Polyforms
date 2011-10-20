package org.polyforms.repository.jpa;

import java.lang.reflect.Method;

import javax.persistence.Query;

/**
 * Strategy of build query for specified method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface QueryBuilder {
    /**
     * Type of query built by query builder.
     * 
     */
    enum QueryType {
        SELECT, UPDATE, DELETE, COUNT
    }

    /**
     * Build {@link Query} from specified method.
     * 
     * @param type of query
     * @param entityClass class of entity
     * @param method to create a query
     * @return Query built from method.
     * 
     * @throws IllegalArgumentException if cannot build
     */
    Query build(QueryType type, Class<?> entityClass, Method method);
}
