package org.polyforms.repository.spring;

import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.polyforms.repository.support.GenericEntityClassResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring bean configuration for buildin {@link Repository}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Configuration
public class RepositoryConfiguration {
    private final GenericEntityClassResolver genericEntityClassResolver = new GenericEntityClassResolver(
            Repository.class);

    /**
     * Register implementation of {@link EntityClassResolver}.
     * 
     * @return entityClassResolver
     */
    @Bean
    public EntityClassResolver entityClassResolver() {
        return genericEntityClassResolver;
    }

    /**
     * Register implementation of {@link RepositoryMatcher}.
     * 
     * @return repositoryMatcher
     */
    @Bean
    public RepositoryMatcher repositoryMatcher() {
        return genericEntityClassResolver;
    }
}
