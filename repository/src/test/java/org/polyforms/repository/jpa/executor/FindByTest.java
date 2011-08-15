package org.polyforms.repository.jpa.executor;

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

public class FindByTest {
    private final List<Object> entities = Collections.singletonList(new Object());
    private QueryExecutor executor;

    @Before
    public void setUp() {
        executor = new FindBy(EasyMock.createMock(EntityClassResolver.class), EasyMock.createMock(QueryBuilder.class),
                EasyMock.createMock(QueryParameterBinder.class));
    }

    @Test
    public void find() {
        final Query query = EasyMock.createMock(Query.class);

        query.getResultList();
        EasyMock.expectLastCall().andReturn(entities);
        EasyMock.replay(query);

        Assert.assertSame(entities, executor.getResult(query));
        EasyMock.verify(query);
    }
}
