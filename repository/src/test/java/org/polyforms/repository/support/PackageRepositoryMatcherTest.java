package org.polyforms.repository.support;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.RepositoryMatcher;

public class PackageRepositoryMatcherTest {
    private RepositoryMatcher repositoryMatcher;

    @Before
    public void setUp() {
        repositoryMatcher = new PackageRepositoryMatcher(new String[] { "org.polyforms.repository" });
    }

    @Test
    public void matches() {
        Assert.assertTrue(repositoryMatcher.matches(PackageRepositoryMatcher.class));
    }

    @Test
    public void notMatches() {
        Assert.assertFalse(repositoryMatcher.matches(String.class));
    }
}
