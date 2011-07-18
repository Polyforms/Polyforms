package org.polyforms.repository.jpa.executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.PaginationProvider;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;

/**
 * Implementation of find method which returns list of matching entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class FindBy extends QueryExecutor {
    private final PaginationProvider paginationProvider;

    /**
     * Create an instance with {@link PaginationProvider}.
     */
    @Inject
    public FindBy(final QueryBuilder queryBuilder, final QueryParameterBinder queryParameterBinder,
            final PaginationProvider paginationProvider) {
        super(queryBuilder, queryParameterBinder);
        this.paginationProvider = paginationProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getResult(final Query query) {
        query.setFirstResult(paginationProvider.getFirstResult());
        query.setMaxResults(paginationProvider.getMaxResults());
        return query.getResultList();
    }
}
