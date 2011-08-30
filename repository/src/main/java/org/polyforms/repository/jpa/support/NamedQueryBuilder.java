package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.spi.Executor;

/**
 * Implementation of {@link QueryBuilder} for NamedQuery.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class NamedQueryBuilder implements QueryBuilder {
    private final QueryResolver queryNameResolver = new EntityClassPrefixingQueryNameResolver();
    private final EntityManager entityManager;

    public NamedQueryBuilder(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    public Query build(final Executor executor, final Class<?> entityClass, final Method method) {
        final String queryName = queryNameResolver.getQuery(entityClass, method);
        return entityManager.createNamedQuery(queryName);
    }
}
