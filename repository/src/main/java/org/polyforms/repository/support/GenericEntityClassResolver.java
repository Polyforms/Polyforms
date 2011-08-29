package org.polyforms.repository.support;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.util.GenericsUtils;
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
    private final int position;
    private final Class<?> genericInterface;

    /**
     * Create a default instance.
     */
    public GenericEntityClassResolver() {
        this(null);
    }

    /**
     * Create an instance with the specified interface implemented by Repository.
     * 
     * The interface MUST be generic and the first generic type is for entity class.
     * 
     * @param genericInterface implemented by Repository.
     */
    public GenericEntityClassResolver(final Class<?> genericInterface) {
        this(genericInterface, 0);
    }

    /**
     * Create an instance with the specified interface implemented by Repository.
     * 
     * The interface MUST be generic and the first generic type is for entity class.
     * 
     * @param genericInterface implemented by Repository.
     * @param position the position of entity class in specified interface
     */
    public GenericEntityClassResolver(final Class<?> genericInterface, final int position) {
        this.genericInterface = genericInterface;
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> resolve(final Class<?> repositoryClass) {
        if (!resolvedEntityClassCache.containsKey(repositoryClass)) {
            LOGGER.trace("Cache missed when resolving entity class for {}.", repositoryClass);
            final Class<?>[] cadidates;
            if (genericInterface == null) {
                cadidates = GenericsUtils.resolveTypeArguments(repositoryClass);
            } else {
                cadidates = GenericTypeResolver.resolveTypeArguments(repositoryClass, genericInterface);
            }
            Assert.notEmpty(cadidates, "The entity class of repository[" + repositoryClass.getName()
                    + "] is not found. Please check the configuration of repository.");
            Assert.isTrue(position < cadidates.length, "Require position " + position
                    + ",but size of parameterized types is " + cadidates.length);
            resolvedEntityClassCache.put(repositoryClass, cadidates[position]);
        }
        final Class<?> entityClass = resolvedEntityClassCache.get(repositoryClass);
        LOGGER.debug("Resolved entity class {} for {}.", entityClass, repositoryClass);
        return entityClass;
    }
}
