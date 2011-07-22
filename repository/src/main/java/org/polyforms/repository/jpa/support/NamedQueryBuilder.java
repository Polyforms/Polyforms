package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryNameResolver;

/**
 * Implementation of {@link QueryBuilder} for NamedQuery.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class NamedQueryBuilder implements QueryBuilder {
    private final QueryNameResolver queryNameResolver;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an instance with {@link QueryNameResolver}.
     */
    @Inject
    public NamedQueryBuilder(final QueryNameResolver queryNameResolver) {
        this.queryNameResolver = queryNameResolver;
    }

    /**
     * {@inheritDoc}
     */
    public Query build(final Method method) {
        final String queryName = queryNameResolver.getQueryName(method);
        return entityManager.createNamedQuery(queryName);
    }
}
