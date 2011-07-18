package org.polyforms.repository.support;

import java.util.HashMap;
import java.util.Map;

import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.springframework.core.GenericTypeResolver;

/**
 * Strategy of resolving entity's class for method which declares in classes inherited from generic interface.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class GenericEntityClassResolver implements EntityClassResolver, RepositoryMatcher {
    private final Map<Class<?>, Class<?>> resolvedEntityClassCache = new HashMap<Class<?>, Class<?>>();
    private final Class<?> genericInterface;

    public GenericEntityClassResolver(final Class<?> genericInterface) {
        this.genericInterface = genericInterface;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> resolve(final Class<?> repositoryClass) {
        if (!resolvedEntityClassCache.containsKey(repositoryClass)) {
            final Class<?> entityClass = GenericTypeResolver.resolveTypeArgument(repositoryClass, genericInterface);
            if (entityClass == null) {
                throw new IllegalArgumentException("The entity class of repository[" + repositoryClass.getName()
                        + "] is not found. Please check the configuration of repository.");
            }
            resolvedEntityClassCache.put(repositoryClass, entityClass);
        }
        return resolvedEntityClassCache.get(repositoryClass);
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Class<?> candidate) {
        return genericInterface.isAssignableFrom(candidate);
    }
}
