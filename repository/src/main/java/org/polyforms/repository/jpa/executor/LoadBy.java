package org.polyforms.repository.jpa.executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;

/**
 * Implementation of get method which returns one matching entity.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class LoadBy extends QueryExecutor {
    @Inject
    public LoadBy(final QueryBuilder queryBuilder, final QueryParameterBinder queryParameterBinder) {
        super(queryBuilder, queryParameterBinder);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws NoResultException if there is no result
     * @throws NonUniqueResultException if more than one entities matching searching criteria
     */
    @Override
    protected Object getResult(final Query query) throws NoResultException, NonUniqueResultException {
        return query.getSingleResult();
    }
}
