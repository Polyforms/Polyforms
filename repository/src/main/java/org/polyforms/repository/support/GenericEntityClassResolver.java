package org.polyforms.repository.support;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.polyforms.repository.spi.EntityClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

/**
 * Strategy of resolving entity's class for method which declares in classes inherited from generic interface.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class GenericEntityClassResolver implements EntityClassResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericEntityClassResolver.class);
    private final Map<Class<?>, Class<?>> resolvedEntityClassCache = new HashMap<Class<?>, Class<?>>();
    private int position;
    private Class<?> genericInterface;

    /**
     * {@inheritDoc}
     */
    public Class<?> resolve(final Class<?> repositoryClass) {
        if (!resolvedEntityClassCache.containsKey(repositoryClass)) {
            LOGGER.trace("Cache missed when resolving entity class for {}.", repositoryClass);
            final Class<?>[] entityClasses = doResolve(repositoryClass);
            Assert.notNull(entityClasses, "The entity class of repository[" + repositoryClass.getName()
                    + "] is not found. Please check the configuration of repository.");
            resolvedEntityClassCache.put(repositoryClass, entityClasses[position]);
        }
        final Class<?> entityClass = resolvedEntityClassCache.get(repositoryClass);
        LOGGER.debug("Resolved entity class {} for {}.", entityClass, repositoryClass);
        return entityClass;
    }

    private Class<?>[] doResolve(final Class<?> repositoryClass) {
        if (genericInterface != null) {
            return GenericTypeResolver.resolveTypeArguments(repositoryClass, genericInterface);
        }

        Class<?>[] entityClasses = resolveFromSuperclass(repositoryClass);

        if (entityClasses == null) {
            entityClasses = resolveFromInterfaces(repositoryClass, repositoryClass.getInterfaces());
        }

        return entityClasses;
    }

    private Class<?>[] resolveFromSuperclass(final Class<?> repositoryClass) {
        final Class<?> superclass = repositoryClass.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return GenericTypeResolver.resolveTypeArguments(repositoryClass, superclass);
    }

    private Class<?>[] resolveFromInterfaces(final Class<?> repositoryClass, final Class<?>[] interfaces) {
        for (final Class<?> interfaze : interfaces) {
            Class<?>[] entityClasses = GenericTypeResolver.resolveTypeArguments(repositoryClass, interfaze);
            if (entityClasses == null) {
                entityClasses = resolveFromInterfaces(repositoryClass, interfaze.getInterfaces());
            }
            if (entityClasses != null) {
                return entityClasses;
            }
        }
        return null;
    }

    /**
     * Set the specified interface implemented by Repository.
     * 
     * The interface MUST be generic and the first generic type is for entity class.
     * 
     * @param genericInterface implemented by Repository.
     */
    public void setGenericInterface(final Class<?> genericInterface) {
        this.genericInterface = genericInterface;
    }
}
