package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;

@Named
public class PrioritizedQueryBuilder implements QueryBuilder {
    private final NamedQueryBuilder namedQueryBuilder;
    private final JpqlQueryBuilder jpqlQueryBuilder;

    @Inject
    public PrioritizedQueryBuilder(final NamedQueryBuilder namedQueryBuilder, final JpqlQueryBuilder jpqlQueryBuilder) {
        this.namedQueryBuilder = namedQueryBuilder;
        this.jpqlQueryBuilder = jpqlQueryBuilder;
    }

    public Query build(final String executorName, final Class<?> entityClass, final Method method) {
        Query query = namedQueryBuilder.build(entityClass, method);
        if (query == null) {
            query = jpqlQueryBuilder.build(executorName, entityClass, method);
        }
        return query;
    }
}
