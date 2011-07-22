package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryNameResolver;
import org.springframework.test.util.ReflectionTestUtils;

public class NamedQueryBuilderTest {
    private QueryBuilder queryBuilder;
    private EntityManager entityManager;
    private QueryNameResolver queryNameResolver;

    @Before
    public void setUp() {
        queryNameResolver = EasyMock.createMock(QueryNameResolver.class);
        queryBuilder = new NamedQueryBuilder(queryNameResolver);

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(queryBuilder, "entityManager", entityManager);
    }

    @Test
    public void build() {
        final Method method = null;
        final String queryName = "Mock.getByName";
        final Query query = EasyMock.createMock(Query.class);

        queryNameResolver.getQueryName(method);
        EasyMock.expectLastCall().andReturn(queryName);
        entityManager.createNamedQuery(queryName);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(queryNameResolver, entityManager);

        Assert.assertSame(query, queryBuilder.build(method));
        EasyMock.verify(queryNameResolver, entityManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotBuild() {
        final Method method = null;
        final String queryName = "Mock.getByName";

        queryNameResolver.getQueryName(method);
        EasyMock.expectLastCall().andReturn(queryName);
        entityManager.createNamedQuery(queryName);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(queryNameResolver, entityManager);

        queryBuilder.build(method);
        EasyMock.verify(queryNameResolver, entityManager);
    }
}
