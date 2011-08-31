package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Implementation of find method which returns list of matching entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class FindBy extends QueryExecutor {
    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public FindBy(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
            final QueryParameterBinder queryParameterBinder) {
        super(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getResult(final Method method, final Query query) {
        final Class<?> type = method.getReturnType();

        if (Collection.class.isAssignableFrom(type)) {
            return query.getResultList();
        }

        try {
            return query.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
    }

    @Override
    protected QueryType getQueryType() {
        return QueryType.SELECT;
    }
}
