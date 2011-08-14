package org.polyforms.repository.jpa.executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Implementation of get method which returns one matching entity.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class LoadBy extends QueryExecutor {
    /**
     * Create an instance with {@link QueryBuilder} and {@link QueryParameterBinder}.
     */
    @Inject
    public LoadBy(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
            final QueryParameterBinder queryParameterBinder) {
        super(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    /**
     * @throws NoResultException if there is no result
     * @throws NonUniqueResultException if more than one entities matching searching criteria
     */
    @Override
    protected Object getResult(final Query query) {
        return query.getSingleResult();
    }
}
