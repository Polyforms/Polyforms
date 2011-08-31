package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.jpa.EntityHelper;

@Named
class JpqlQueryBuilder {
    private enum QueryType {
        SELECT, UPDATE, DELETE, COUNT
    }

    private final Map<String, JpqlQueryStringBuilder> queryStringbuilders = new HashMap<String, JpqlQueryStringBuilder>();
    private final String defaultType = getQueryPrefix(QueryType.SELECT);
    private final ExecutorPrefix executorPrefix;
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public JpqlQueryBuilder(final ExecutorPrefix executorPrefix, final EntityHelper entityHelper) {
        this.executorPrefix = executorPrefix;
        queryStringbuilders.put(getQueryPrefix(QueryType.SELECT), new SelectQueryStringBuilder());
        queryStringbuilders.put(getQueryPrefix(QueryType.UPDATE), new UpdateQueryStringBuilder());
        queryStringbuilders.put(getQueryPrefix(QueryType.DELETE), new DeleteQueryStringBuilder());
        queryStringbuilders.put(getQueryPrefix(QueryType.COUNT), new CountQueryStringBuilder(entityHelper));
    }

    private String getQueryPrefix(final QueryType type) {
        return type.name().toLowerCase(Locale.getDefault());
    }

    /**
     * {@inheritDoc}
     */
    public Query build(final String executorName, final Class<?> entityClass, final Method method) {
        final String queryString = getJpqlQueryStringBuilder(executorName).getQuery(entityClass,
                nomalizeQueryString(executorName, method.getName()));
        return entityManager.createQuery(queryString);
    }

    private String nomalizeQueryString(final String executorName, final String queryString) {
        for (final String prefix : executorPrefix.getPrefix(executorName)) {
            if (queryString.startsWith(prefix)) {
                return queryString.substring(prefix.length());
            }
        }

        return queryString;
    }

    private JpqlQueryStringBuilder getJpqlQueryStringBuilder(final String executorName) {
        final String prefix = executorPrefix.convertToPrefix(executorName);
        if (queryStringbuilders.containsKey(prefix)) {
            return queryStringbuilders.get(prefix);
        }

        return queryStringbuilders.get(defaultType);
    }
}
