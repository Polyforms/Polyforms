package org.polyforms.repository.jpa.query;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link QueryBuilder} for JPA 2.0.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class PrioritizedQueryBuilder implements QueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrioritizedQueryBuilder.class);
    private final NamedQueryBuilder namedQueryBuilder;
    private final JpqlQueryBuilder jpqlQueryBuilder;

    /**
     * Create an instance with implementation of query builder.
     */
    @Inject
    public PrioritizedQueryBuilder(final NamedQueryBuilder namedQueryBuilder, final JpqlQueryBuilder jpqlQueryBuilder) {
        this.namedQueryBuilder = namedQueryBuilder;
        this.jpqlQueryBuilder = jpqlQueryBuilder;
    }

    /**
     * {@inheritDoc}
     */
    public Query build(final QueryType type, final Class<?> entityClass, final Method method) {
        Query query = namedQueryBuilder.build(entityClass, method);
        if (query == null) {
            LOGGER.debug("Cannot find named query for {}. Creating query by parsing it.", method);
            query = jpqlQueryBuilder.build(type, entityClass, method);
        }
        return query;
    }
}
