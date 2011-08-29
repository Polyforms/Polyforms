package org.polyforms.repository.spring;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.RepositoryMatcher;

public class RepositoryConfigurationTest {
    private final RepositoryConfiguration configuration = new RepositoryConfiguration();

    @Test
    public void repositoryMatcher() {
        final RepositoryMatcher repositoryMatcher = configuration.repositoryMatcher();
        Assert.assertTrue(repositoryMatcher.matches(MockRepository.class));
    }

    public static interface MockRepository extends Repository<Object> {
    }
}
