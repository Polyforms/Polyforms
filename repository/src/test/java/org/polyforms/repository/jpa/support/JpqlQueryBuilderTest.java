package org.polyforms.repository.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.springframework.test.util.ReflectionTestUtils;

public class JpqlQueryBuilderTest {
    private QueryBuilder queryBuilder;
    private EntityManager entityManager;
    private QueryResolver queryStringbuilder;

    @Before
    public void setUp() {
        entityManager = EasyMock.createMock(EntityManager.class);
        queryBuilder = new JpqlQueryBuilder(entityManager);
        queryStringbuilder = EasyMock.createMock(QueryResolver.class);
        ReflectionTestUtils.setField(queryBuilder, "queryStringbuilder", queryStringbuilder);
    }

    @Test
    public void build() {
        final String queryString = "select m from Mock m where m.name = ?1";
        final Query query = EasyMock.createMock(Query.class);

        queryStringbuilder.getQuery(null, null);
        EasyMock.expectLastCall().andReturn(queryString);
        entityManager.createQuery(queryString);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(queryStringbuilder, entityManager);

        Assert.assertSame(query, queryBuilder.build(null, null, null));
        EasyMock.verify(queryStringbuilder, entityManager);
    }
}
