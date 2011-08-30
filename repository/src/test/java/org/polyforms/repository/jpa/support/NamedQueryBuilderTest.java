package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.springframework.test.util.ReflectionTestUtils;

public class NamedQueryBuilderTest {
    private QueryBuilder queryBuilder;
    private EntityManager entityManager;
    private QueryResolver queryNameResolver;

    @Before
    public void setUp() {
        entityManager = EasyMock.createMock(EntityManager.class);
        queryBuilder = new NamedQueryBuilder(entityManager);
        queryNameResolver = EasyMock.createMock(QueryResolver.class);
        ReflectionTestUtils.setField(queryBuilder, "queryNameResolver", queryNameResolver);
    }

    @Test
    public void build() {
        final Method method = null;
        final String queryName = "Mock.getByName";
        final Query query = EasyMock.createMock(Query.class);

        queryNameResolver.getQuery(null, method);
        EasyMock.expectLastCall().andReturn(queryName);
        entityManager.createNamedQuery(queryName);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(queryNameResolver, entityManager);

        Assert.assertSame(query, queryBuilder.build(null, null, method));
        EasyMock.verify(queryNameResolver, entityManager);
    }
}
