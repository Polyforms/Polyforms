package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Implementation of method which updates entities.
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
    public UpdateBy(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
            final QueryParameterBinder queryParameterBinder) {
        super(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    @Override
    protected Object getResult(final Method method, final Query query) {
        return query.executeUpdate();
    }

    @Override
    protected QueryType getQueryType() {
        return QueryType.UPDATE;
    }
}
