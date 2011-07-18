package org.polyforms.repository.spring;

import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.polyforms.repository.support.GenericEntityClassResolver;
import org.polyforms.repository.support.InheritedRepositoryMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {
    @Bean
    public EntityClassResolver entityClassResolver() {
        return new GenericEntityClassResolver(Repository.class);
    }

    @Bean
    public RepositoryMatcher repositoryMatcher() {
        return new InheritedRepositoryMatcher(Repository.class);
    }
}
