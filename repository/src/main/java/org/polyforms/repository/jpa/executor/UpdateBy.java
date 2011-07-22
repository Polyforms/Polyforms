package org.polyforms.repository.jpa.executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;

/**
 * Implementation of method which updates or removes entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class UpdateBy extends QueryExecutor {
    /**
     * Create an instance with {@link QueryBuilder} and {@link QueryParameterBinder}.
     */
    @Inject
    public UpdateBy(final QueryBuilder queryBuilder, final QueryParameterBinder queryParameterBinder) {
        super(queryBuilder, queryParameterBinder);
    }

    @Override
    protected Object getResult(final Query query) {
        return query.executeUpdate();
    }
}
