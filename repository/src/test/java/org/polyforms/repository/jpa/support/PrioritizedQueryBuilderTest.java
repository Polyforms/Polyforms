package org.polyforms.repository.jpa.support;

import java.util.Collections;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.After;
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

    @After
    public void tearDown() {
        EasyMock.verify(queryBuilder);
    }

    @Test
    public void build() {
        final Query query = EasyMock.createMock(Query.class);
        queryBuilder.build(null, null);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(queryBuilder);

        Assert.assertSame(query, prioritizedQueryBuilder.build(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotBuild() {
        queryBuilder.build(null, null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(queryBuilder);

        prioritizedQueryBuilder.build(null, null);
    }
}
