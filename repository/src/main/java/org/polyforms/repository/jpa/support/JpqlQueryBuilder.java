package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.jpa.EntityHelper;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;

@Named
class JpqlQueryBuilder {
    private final Map<QueryType, JpqlQueryStringBuilder> queryStringbuilders = new HashMap<QueryType, JpqlQueryStringBuilder>();
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public JpqlQueryBuilder(final ExecutorPrefix executorPrefix, final EntityHelper entityHelper) {
        queryStringbuilders.put(QueryType.SELECT, new SelectQueryStringBuilder(executorPrefix));
        queryStringbuilders.put(QueryType.UPDATE, new UpdateQueryStringBuilder(executorPrefix));
        queryStringbuilders.put(QueryType.DELETE, new DeleteQueryStringBuilder(executorPrefix));
        queryStringbuilders.put(QueryType.COUNT, new CountQueryStringBuilder(executorPrefix, entityHelper));
    }

    /**
     * {@inheritDoc}
     */
    public Query build(final QueryType type, final Class<?> entityClass, final Method method) {
        final String queryString = queryStringbuilders.get(type).getQuery(entityClass, method.getName());
        return entityManager.createQuery(queryString);
    }
}
