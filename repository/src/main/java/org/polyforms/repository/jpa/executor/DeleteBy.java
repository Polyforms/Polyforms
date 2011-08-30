package org.polyforms.repository.jpa.executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Implementation of method which delete entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class DeleteBy extends QueryExecutor {
    /**
     * Create an instance with {@link QueryBuilder} and {@link QueryParameterBinder}.
     */
    @Inject
    public DeleteBy(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
            final QueryParameterBinder queryParameterBinder) {
        super(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    @Override
    protected Object getResult(final Query query) {
        return query.executeUpdate();
    }
}
