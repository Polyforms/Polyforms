package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.Executor;

/**
 * Abstract implementation of methods which use JPA {@link Query} to do some work in persistence.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public abstract class QueryExecutor implements Executor {
    private final QueryBuilder queryBuilder;
    private final QueryParameterBinder queryParameterBinder;

    protected QueryExecutor(final QueryBuilder queryBuilder, final QueryParameterBinder queryParameterBinder) {
        this.queryBuilder = queryBuilder;
        this.queryParameterBinder = queryParameterBinder;
    }

    /**
     * {@inheritDoc}
     */
    public final Object execute(final Object target, final Method method, final Object... arguments) {
        final Query query = queryBuilder.build(method);
        queryParameterBinder.bind(query, method, arguments);
        return getResult(query);
    }

    protected abstract Object getResult(Query query);
}
