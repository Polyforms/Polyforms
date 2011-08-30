package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.spi.Executor;

class JpqlQueryBuilder implements QueryBuilder {
    private final QueryResolver queryStringbuilder = new JpqlQueryStringBuilder();
    private final EntityManager entityManager;

    public JpqlQueryBuilder(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    public Query build(final Executor executor, final Class<?> entityClass, final Method method) {
        final String queryString = queryStringbuilder.getQuery(entityClass, method);
        return entityManager.createQuery(queryString);
    }
}
