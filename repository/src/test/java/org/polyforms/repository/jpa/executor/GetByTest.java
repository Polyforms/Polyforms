package org.polyforms.repository.jpa.executor;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;

public class GetByTest {
    private QueryExecutor executor;

    @Before
    public void setUp() {
        executor = new GetBy(EasyMock.createMock(QueryBuilder.class), EasyMock.createMock(QueryParameterBinder.class));
    }

    @Test
    public void get() {
        final Object entity = new Object();

        final Query query = EasyMock.createMock(Query.class);
        query.getSingleResult();
        EasyMock.expectLastCall().andReturn(entity);
        EasyMock.replay(query);

        Assert.assertSame(entity, executor.getResult(query));
        EasyMock.verify(query);
    }

    @Test
    public void getWithoutResult() {
        final Query query = EasyMock.createMock(Query.class);
        query.getSingleResult();
        EasyMock.expectLastCall().andThrow(new NoResultException());
        EasyMock.replay(query);

        Assert.assertNull(executor.getResult(query));
        EasyMock.verify(query);
    }
}
