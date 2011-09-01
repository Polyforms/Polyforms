package org.polyforms.repository.jpa.query;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class NamedQueryBuilderTest {
    private NamedQueryBuilder queryBuilder;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        queryBuilder = new NamedQueryBuilder();
        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(queryBuilder, "entityManager", entityManager);
    }

    @Test
    public void build() throws NoSuchMethodException {
        final String queryName = "Object.toString";
        final Query query = EasyMock.createMock(Query.class);

        entityManager.createNamedQuery(queryName);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(entityManager);

        Assert.assertSame(query, queryBuilder.build(Object.class, Object.class.getMethod("toString", new Class<?>[0])));
        EasyMock.verify(entityManager);
    }

    @Test
    public void buildForNotExistedQuery() throws NoSuchMethodException {
        final String queryName = "Object.toString";
        entityManager.createNamedQuery(queryName);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(entityManager);

        Assert.assertNull(queryBuilder.build(Object.class, Object.class.getMethod("toString", new Class<?>[0])));
        EasyMock.verify(entityManager);
    }
}
