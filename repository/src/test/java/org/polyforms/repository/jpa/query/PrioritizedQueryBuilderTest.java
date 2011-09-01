package org.polyforms.repository.jpa.query;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;

public class PrioritizedQueryBuilderTest {
    private NamedQueryBuilder namedQueryBuilder;
    private JpqlQueryBuilder jpqlQueryBuilder;
    private QueryBuilder prioritizedQueryBuilder;

    @Before
    public void setUp() {
        namedQueryBuilder = EasyMock.createMock(NamedQueryBuilder.class);
        jpqlQueryBuilder = EasyMock.createMock(JpqlQueryBuilder.class);
        prioritizedQueryBuilder = new PrioritizedQueryBuilder(namedQueryBuilder, jpqlQueryBuilder);
    }

    @Test
    public void buildByName() {
        final Query query = EasyMock.createMock(Query.class);
        namedQueryBuilder.build(null, null);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(namedQueryBuilder);

        Assert.assertSame(query, prioritizedQueryBuilder.build(null, null, null));
        EasyMock.verify(namedQueryBuilder);
    }

    @Test
    public void buildByJpql() {
        final Query query = EasyMock.createMock(Query.class);
        namedQueryBuilder.build(null, null);
        EasyMock.expectLastCall().andReturn(null);
        jpqlQueryBuilder.build(null, null, null);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(namedQueryBuilder, jpqlQueryBuilder);

        Assert.assertSame(query, prioritizedQueryBuilder.build(null, null, null));
        EasyMock.verify(namedQueryBuilder, jpqlQueryBuilder);
    }
}
