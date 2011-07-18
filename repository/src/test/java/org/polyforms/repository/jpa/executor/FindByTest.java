package org.polyforms.repository.jpa.executor;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.PaginationProvider;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;

public class FindByTest {
    private final List<Object> entities = Collections.singletonList(new Object());
    private PaginationProvider paginationProvider;
    private QueryExecutor executor;

    @Before
    public void setUp() {
        paginationProvider = EasyMock.createMock(PaginationProvider.class);
        executor = new FindBy(EasyMock.createMock(QueryBuilder.class), EasyMock.createMock(QueryParameterBinder.class),
                paginationProvider);
    }

    @Test
    public void find() {
        final Query query = EasyMock.createMock(Query.class);

        paginationProvider.getFirstResult();
        EasyMock.expectLastCall().andReturn(0);
        query.setFirstResult(0);
        EasyMock.expectLastCall().andReturn(query);
        paginationProvider.getMaxResults();
        EasyMock.expectLastCall().andReturn(10);
        query.setMaxResults(10);
        EasyMock.expectLastCall().andReturn(query);
        query.getResultList();
        EasyMock.expectLastCall().andReturn(entities);
        EasyMock.replay(query, paginationProvider);

        Assert.assertSame(entities, executor.getResult(query));
        EasyMock.verify(query, paginationProvider);
    }
}
