package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Strategy of resolving query's name with prefix which is name of entity class for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
final class EntityClassPrefixingQueryNameResolver implements QueryResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityClassPrefixingQueryNameResolver.class);
    private final Map<Method, String> queryNameCache = new HashMap<Method, String>();

    /**
     * {@inheritDoc}
     */
    public String getQuery(final Class<?> entityClass, final Method method) {
        if (!queryNameCache.containsKey(method)) {
            LOGGER.trace("Cache missed when get name of query for {}.", method);
            queryNameCache.put(method, entityClass.getSimpleName() + "." + method.getName());
        }
        final String queryName = queryNameCache.get(method);
        LOGGER.debug("Name of query for {} is {}", method, queryName);
        return queryName;
    }
}
