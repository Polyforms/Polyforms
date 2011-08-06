package org.polyforms.repository.jpa.executor;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;

public class UpdateByTest {
    private QueryExecutor executor;

    @Before
    public void setUp() {
        executor = new UpdateBy(EasyMock.createMock(QueryBuilder.class),
                EasyMock.createMock(QueryParameterBinder.class));
    }

    @Test
    public void update() {
        final Query query = EasyMock.createMock(Query.class);
        query.executeUpdate();
        EasyMock.expectLastCall().andReturn(2);
        EasyMock.replay(query);

        Assert.assertEquals(2, executor.getResult(query));
        EasyMock.verify(query);
    }
}
