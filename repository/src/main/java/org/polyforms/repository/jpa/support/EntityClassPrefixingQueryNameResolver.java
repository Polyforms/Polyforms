package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.jpa.QueryNameResolver;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Strategy of resolving query's name with prefix which is name of entity class for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class EntityClassPrefixingQueryNameResolver implements QueryNameResolver {
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
            final Class<?> entityClass = entityClassResolver.resolve(method.getDeclaringClass());
            queryNameCache.put(method, entityClass.getSimpleName() + "." + method.getName());
        }
        return queryNameCache.get(method);
    }
}
