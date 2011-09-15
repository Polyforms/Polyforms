package org.polyforms.repository.jpa.query;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create Query by parsing string.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
class JpqlQueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpqlQueryStringBuilder.class);
    private final Map<QueryType, JpqlQueryStringBuilder> queryStringbuilders = new HashMap<QueryType, JpqlQueryStringBuilder>();
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    protected JpqlQueryBuilder(final ExecutorPrefixHolder executorPrefix) {
        queryStringbuilders.put(QueryType.SELECT, new SelectQueryStringBuilder(executorPrefix));
        queryStringbuilders.put(QueryType.UPDATE, new UpdateQueryStringBuilder(executorPrefix));
        queryStringbuilders.put(QueryType.DELETE, new DeleteQueryStringBuilder(executorPrefix));
        queryStringbuilders.put(QueryType.COUNT, new CountQueryStringBuilder(executorPrefix));
    }

    protected Query build(final QueryType type, final Class<?> entityClass, final Method method) {
        final String methodName = method.getName();
        final String queryString = queryStringbuilders.get(type).getQuery(entityClass, methodName);
        LOGGER.debug("The query string of type {} parsed by {} from {} is {}.", new Object[] { type, methodName,
                queryString });

        return entityManager.createQuery(queryString);
    }
}
