package org.polyforms.repository.spi;

/**
 * Strategy of finding repository to apply interceptor.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface RepositoryMatcher {
    /**
     * Check whether the specified class is a repository.
     * 
     * @param candidate class for repository
     * 
     * @return true if candidate is a repository, false if not
     */
    boolean matches(Class<?> candidate);
}
