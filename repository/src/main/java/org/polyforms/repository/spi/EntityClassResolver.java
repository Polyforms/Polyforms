package org.polyforms.repository.spi;

/**
 * Resolver of entity class from repository class.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface EntityClassResolver {
    /**
     * Resolve entity class from repository class.
     * 
     * @param repositoryClass which stores entity instance
     * 
     * @return entity class stored in specified repository or null if the entity class cannot be resolved
     * 
     * @throws IllegalArgumentException if not resolvable
     */
    Class<?> resolve(final Class<?> repositoryClass);
}
