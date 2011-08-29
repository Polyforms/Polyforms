package org.polyforms.repository.spring;

import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.polyforms.repository.support.InheritedRepositoryMatcher;
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
    /**
     * Register implementation of {@link RepositoryMatcher}.
     * 
     * @return repositoryMatcher
     */
    @Bean
    public RepositoryMatcher repositoryMatcher() {
        return new InheritedRepositoryMatcher(Repository.class);
    }
}
