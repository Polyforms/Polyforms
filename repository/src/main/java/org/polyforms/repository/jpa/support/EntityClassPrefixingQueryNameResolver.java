package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.jpa.QueryNameResolver;
import org.polyforms.repository.spi.EntityClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Strategy of resolving query's name with prefix which is name of entity class for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class EntityClassPrefixingQueryNameResolver implements QueryNameResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityClassPrefixingQueryNameResolver.class);
    private final Map<Method, String> queryNameCache = new HashMap<Method, String>();
    private final EntityClassResolver entityClassResolver;

    /**
     * Create an instance with {@link EntityClassResolver}s.
     */
    @Inject
    public EntityClassPrefixingQueryNameResolver(final EntityClassResolver entityClassResolver) {
        this.entityClassResolver = entityClassResolver;
    }

    /**
     * {@inheritDoc}
     */
    public String getQueryName(final Method method) {
        if (!queryNameCache.containsKey(method)) {
            LOGGER.trace("Cache missed when get name of query for {}.", method);
            final Class<?> entityClass = entityClassResolver.resolve(method.getDeclaringClass());
            queryNameCache.put(method, entityClass.getSimpleName() + "." + method.getName());
        }
        final String queryName = queryNameCache.get(method);
        LOGGER.debug("Name of query for {} is {}", method, queryName);
        return queryName;
    }
}
