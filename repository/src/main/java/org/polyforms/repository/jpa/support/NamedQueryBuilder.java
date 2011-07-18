package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryNameResolver;

@Named
public class NamedQueryBuilder implements QueryBuilder {
    @PersistenceContext
    private EntityManager entityManager;
    private final QueryNameResolver queryNameResolver;

    @Inject
    public NamedQueryBuilder(final QueryNameResolver queryNameResolver) {
        this.queryNameResolver = queryNameResolver;
    }

    public Query build(final Method method) {
        final String queryName = queryNameResolver.getQueryName(method);
        return entityManager.createNamedQuery(queryName);
    }
}
