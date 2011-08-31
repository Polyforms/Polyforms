package org.polyforms.repository.jpa.executor;

import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

public class FindByTest {
    private final List<Object> entities = Collections.singletonList(new Object());
    private QueryExecutor executor;

    @Before
    public void setUp() {
        executor = new FindBy(EasyMock.createMock(EntityClassResolver.class), EasyMock.createMock(QueryBuilder.class),
                EasyMock.createMock(QueryParameterBinder.class));
    }

    @Test
    public void find() throws NoSuchMethodException {
        final Query query = EasyMock.createMock(Query.class);

        query.getResultList();
        EasyMock.expectLastCall().andReturn(entities);
        EasyMock.replay(query);

        Assert.assertSame(entities,
                executor.getResult(Repository.class.getMethod("find", new Class<?>[] { Object[].class }), query));
        EasyMock.verify(query);
    }

    @Test
    public void get() throws NoSuchMethodException {
        final Object entity = new Object();

        final Query query = EasyMock.createMock(Query.class);
        query.getSingleResult();
        EasyMock.expectLastCall().andReturn(entity);
        EasyMock.replay(query);

        Assert.assertSame(entity,
                executor.getResult(Repository.class.getMethod("find", new Class<?>[] { Object.class }), query));
        EasyMock.verify(query);
    }

    @Test
    public void getWithoutResult() throws NoSuchMethodException {
        final Query query = EasyMock.createMock(Query.class);
        query.getSingleResult();
        EasyMock.expectLastCall().andThrow(new NoResultException());
        EasyMock.replay(query);

        Assert.assertNull(executor.getResult(Repository.class.getMethod("find", new Class<?>[] { Object.class }), query));
        EasyMock.verify(query);
    }

    private static interface Repository {
        Object find(Object id);

        List<Object> find(Object... ids);
    }

    @Test
    public void getQueryType() {
        Assert.assertSame(QueryType.SELECT, executor.getQueryType());
    }
}
