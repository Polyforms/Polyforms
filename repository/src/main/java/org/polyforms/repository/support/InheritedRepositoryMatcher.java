package org.polyforms.repository.support;

import org.polyforms.repository.spi.RepositoryMatcher;
import org.springframework.util.Assert;

/**
 * Strategy of finding repository inheriting Repository.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class InheritedRepositoryMatcher implements RepositoryMatcher {
    private final Class<?> superInterface;

    public InheritedRepositoryMatcher(final Class<?> superInterface) {
        Assert.notNull(superInterface);
        this.superInterface = superInterface;
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Class<?> candidate) {
        return superInterface.isAssignableFrom(candidate);
    }
}
