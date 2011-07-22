package org.polyforms.repository.support;

import java.util.HashMap;
import java.util.Map;

import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

/**
 * Strategy of resolving entity's class for method which declares in classes inherited from generic interface.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class GenericEntityClassResolver implements EntityClassResolver, RepositoryMatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericEntityClassResolver.class);
    private final Map<Class<?>, Class<?>> resolvedEntityClassCache = new HashMap<Class<?>, Class<?>>();
    private final Class<?> genericInterface;

    /**
     * Create an instance for specified interface implemented by Repository.
     * 
     * The interface MUST be generic and the first generic type is for entity class.
     * 
     * @param genericInterface implemented by Repository.
     */
    public GenericEntityClassResolver(final Class<?> genericInterface) {
        LOGGER.trace("Create GenericEntityClassResolver for classed implementing {}", genericInterface);
        this.genericInterface = genericInterface;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> resolve(final Class<?> repositoryClass) {
        LOGGER.trace("Resolve entity class for {}.", repositoryClass);
        if (!resolvedEntityClassCache.containsKey(repositoryClass)) {
            LOGGER.trace("Cache missed when resolving entity class for {}.", repositoryClass);
            final Class<?> entityClass = GenericTypeResolver.resolveTypeArgument(repositoryClass, genericInterface);
            patchForOpenJpa(repositoryClass, entityClass);
            resolvedEntityClassCache.put(repositoryClass, entityClass);
        }
        Class<?> entityClass = resolvedEntityClassCache.get(repositoryClass);
        LOGGER.debug("Resolved entity class {} for {}.", entityClass, repositoryClass);
        return entityClass;
    }

    private void patchForOpenJpa(final Class<?> repositoryClass, final Class<?> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class of repository[" + repositoryClass.getName()
                    + "] is not found. Please check the configuration of repository.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Class<?> candidate) {
        boolean isRepository = genericInterface.isAssignableFrom(candidate);
        LOGGER.debug("{} is a repository.", candidate, isRepository ? "" : "not ");
        return isRepository;
    }
}
