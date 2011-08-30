package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.spi.Executor;

@Named
public class PrioritizedQueryBuilder implements QueryBuilder {
    private final List<QueryBuilder> queryBuilders = new ArrayList<QueryBuilder>();
    @PersistenceContext
    private EntityManager entityManager;

    protected List<QueryBuilder> getqueryBuilders() {
        synchronized (queryBuilders) {
            if (queryBuilders.isEmpty()) {
                queryBuilders.add(new NamedQueryBuilder(entityManager));
                queryBuilders.add(new JpqlQueryBuilder(entityManager));
            }
        }
        return queryBuilders;
    }

    public Query build(final Executor executor, final Class<?> entityClass, final Method method) {
        for (final QueryBuilder queryBuilder : getqueryBuilders()) {
            try {
                return queryBuilder.build(executor, entityClass, method);
            } catch (final IllegalArgumentException e) {
                // Ignore to build with next query builder
            }
        }

        throw new IllegalArgumentException("The query for " + method + " cannot be built.");
    }
}
