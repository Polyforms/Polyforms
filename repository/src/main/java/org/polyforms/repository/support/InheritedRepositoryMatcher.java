package org.polyforms.repository.support;

import org.polyforms.repository.spi.RepositoryMatcher;
import org.springframework.util.Assert;

/**
 * Strategy of finding Repository by inheritance.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class InheritedRepositoryMatcher implements RepositoryMatcher {
    private final Class<?> superInterface;

    /**
     * Create an instance with super class or interface of Repository.
     */
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
