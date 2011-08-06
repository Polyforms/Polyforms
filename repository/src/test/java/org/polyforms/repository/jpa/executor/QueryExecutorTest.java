package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.Executor;

public class QueryExecutorTest {
    private final List<Object> entities = Collections.singletonList(new Object());
    private Query query;

    private QueryBuilder queryBuilder;
    private QueryParameterBinder queryParameterBinder;
    private Executor executor;

    @Before
    public void setUp() {
        queryBuilder = EasyMock.createMock(QueryBuilder.class);
        queryParameterBinder = EasyMock.createMock(QueryParameterBinder.class);

        query = EasyMock.createMock(Query.class);
        executor = new QueryExecutor(queryBuilder, queryParameterBinder) {
            @Override
            protected Object getResult(final Query query) {
                return entities;
            }
        };
    }

    @Test
    public void query() {
        final Object repository = new Object();
        final Method method = null;
        final Object[] arguments = null;

        queryBuilder.build(method);
        EasyMock.expectLastCall().andReturn(query);
        queryParameterBinder.bind(query, method, arguments);
        EasyMock.replay(queryBuilder, queryParameterBinder);

        Assert.assertSame(entities, executor.execute(repository, method, arguments));
        EasyMock.verify(queryBuilder, queryParameterBinder);
    }
}
