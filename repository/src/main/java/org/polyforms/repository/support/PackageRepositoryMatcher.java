package org.polyforms.repository.support;

import org.polyforms.repository.spi.RepositoryMatcher;
import org.polyforms.util.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Strategy of finding Repository by packages.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class PackageRepositoryMatcher implements RepositoryMatcher {
    private final String[] basePackages;

    /**
     * Create an instance with packages belongs Repository.
     */
    public PackageRepositoryMatcher(final String[] basePackages) {
        Assert.notEmpty(basePackages);
        this.basePackages = ArrayUtils.clone(basePackages);
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Class<?> candidate) {
        for (final String basePackage : basePackages) {
            if (candidate.getName().startsWith(basePackage)) {
                return true;
            }
        }
        return false;
    }
}
