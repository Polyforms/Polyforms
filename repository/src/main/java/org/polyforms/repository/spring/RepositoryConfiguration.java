package org.polyforms.repository.spring;

import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.polyforms.repository.support.GenericEntityClassResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {
    private final GenericEntityClassResolver genericEntityClassResolver = new GenericEntityClassResolver(
            Repository.class);

    @Bean
    public EntityClassResolver entityClassResolver() {
        return genericEntityClassResolver;
    }

    @Bean
    public RepositoryMatcher repositoryMatcher() {
        return genericEntityClassResolver;
    }
}
