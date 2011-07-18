package org.polyforms.repository.support;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.RepositoryMatcher;

public class InheritedRepositoryMatcherTest {
    private RepositoryMatcher repositoryMatcher;

    @Before
    public void setUp() {
        repositoryMatcher = new GenericEntityClassResolver(Number.class);
    }

    @Test
    public void matches() {
        Assert.assertTrue(repositoryMatcher.matches(Integer.class));
    }

    @Test
    public void notMatches() {
        Assert.assertFalse(repositoryMatcher.matches(Object.class));
    }
}
