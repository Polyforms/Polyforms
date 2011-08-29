package org.polyforms.repository.support;

import javax.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.RepositoryMatcher;

public class AnnotatedRepositoryMatcherTest {
    private RepositoryMatcher repositoryMatcher;

    @Before
    public void setUp() {
        repositoryMatcher = new AnnotatedRepositoryMatcher(Named.class);
    }

    @Test
    public void matches() {
        Assert.assertTrue(repositoryMatcher.matches(Repository.class));
    }

    @Test
    public void notMatches() {
        Assert.assertFalse(repositoryMatcher.matches(String.class));
    }

    @Named
    private interface Repository {
    }
}
