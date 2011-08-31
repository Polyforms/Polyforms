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
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;

public class QueryExecutorTest {
    private final List<Object> entities = Collections.singletonList(new Object());
    private Query query;
    private EntityClassResolver entityClassResolver;
    private QueryBuilder queryBuilder;
    private QueryParameterBinder queryParameterBinder;
    private Executor executor;

    @Before
    public void setUp() {
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        queryBuilder = EasyMock.createMock(QueryBuilder.class);
        queryParameterBinder = EasyMock.createMock(QueryParameterBinder.class);

        query = EasyMock.createMock(Query.class);
        executor = new MockExecutor(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    @Test
    public void query() {
        final Object repository = new Object();
        final Method method = null;
        final Object[] arguments = null;

        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        queryBuilder.build("MockExecutor", Object.class, method);
        EasyMock.expectLastCall().andReturn(query);
        queryParameterBinder.bind(query, method, arguments);
        EasyMock.replay(entityClassResolver, queryBuilder, queryParameterBinder);

        Assert.assertSame(entities, executor.execute(repository, method, arguments));
        EasyMock.verify(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    private final class MockExecutor extends QueryExecutor {
        private MockExecutor(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
                final QueryParameterBinder queryParameterBinder) {
            super(entityClassResolver, queryBuilder, queryParameterBinder);
        }

        @Override
        protected Object getResult(final Method method, final Query query) {
            return entities;
        }
    }
}
