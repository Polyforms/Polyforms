package org.polyforms.repository.jpa.executor;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

public class DeleteByTest {
    private QueryExecutor executor;

    @Before
    public void setUp() {
        executor = new DeleteBy(EasyMock.createMock(EntityClassResolver.class),
                EasyMock.createMock(QueryBuilder.class), EasyMock.createMock(QueryParameterBinder.class));
    }

    @Test
    public void update() {
        final Query query = EasyMock.createMock(Query.class);
        query.executeUpdate();
        EasyMock.expectLastCall().andReturn(2);
        EasyMock.replay(query);

        Assert.assertEquals(2, executor.getResult(null, query));
        EasyMock.verify(query);
    }

    @Test
    public void getQueryType() {
        Assert.assertSame(QueryType.DELETE, executor.getQueryType());
    }
}
