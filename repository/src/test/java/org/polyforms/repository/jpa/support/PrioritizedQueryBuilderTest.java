package org.polyforms.repository.jpa.support;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.springframework.test.util.ReflectionTestUtils;

public class PrioritizedQueryBuilderTest {
    private QueryBuilder queryBuilder;
    private QueryBuilder prioritizedQueryBuilder;

    @Before
    public void setUp() {
        queryBuilder = EasyMock.createMock(QueryBuilder.class);
        prioritizedQueryBuilder = new PrioritizedQueryBuilder();
        ReflectionTestUtils.setField(prioritizedQueryBuilder, "queryBuilders", Collections.singletonList(queryBuilder));
    }

    @Test
    public void getQueryBuilders() {
        final PrioritizedQueryBuilder prioritizedQueryBuilder = new PrioritizedQueryBuilder();
        final List<QueryBuilder> queryBuilders = prioritizedQueryBuilder.getqueryBuilders();
        Assert.assertEquals(2, queryBuilders.size());
        Assert.assertTrue(queryBuilders.get(0) instanceof NamedQueryBuilder);
        Assert.assertTrue(queryBuilders.get(1) instanceof JpqlQueryBuilder);
    }

    @Test
    public void build() {
        final Query query = EasyMock.createMock(Query.class);
        queryBuilder.build(null, null);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(queryBuilder);

        Assert.assertSame(query, prioritizedQueryBuilder.build(null, null));
        EasyMock.verify(queryBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotBuild() {
        queryBuilder.build(null, null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(queryBuilder);

        prioritizedQueryBuilder.build(null, null);
        EasyMock.verify(queryBuilder);
    }
}
