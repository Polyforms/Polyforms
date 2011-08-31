package org.polyforms.repository.jpa.executor;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

public class CountByTest {
    private QueryExecutor executor;

    @Before
    public void setUp() {
        executor = new CountBy(EasyMock.createMock(EntityClassResolver.class), EasyMock.createMock(QueryBuilder.class),
                EasyMock.createMock(QueryParameterBinder.class));
    }

    @Test
    public void get() {
        final Query query = EasyMock.createMock(Query.class);
        query.getSingleResult();
        EasyMock.expectLastCall().andReturn(2L);
        EasyMock.replay(query);

        Assert.assertSame(2L, executor.getResult(query));
        EasyMock.verify(query);
    }
}
