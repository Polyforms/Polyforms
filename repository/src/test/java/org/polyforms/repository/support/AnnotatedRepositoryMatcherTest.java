package org.polyforms.repository.support;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.RepositoryMatcher;

public class AnnotatedRepositoryMatcherTest {
    private RepositoryMatcher repositoryMatcher;

    @Before
    public void setUp() {
        repositoryMatcher = new AnnotatedRepositoryMatcher(Deprecated.class);
    }

    @Test
    public void matches() {
        Assert.assertTrue(repositoryMatcher.matches(MockRepository.class));
    }

    @Test
    public void notMatches() {
        Assert.assertFalse(repositoryMatcher.matches(String.class));
    }

    public static class MockRepository implements Repository {
    }

    @Deprecated
    public interface Repository {
    }
}
