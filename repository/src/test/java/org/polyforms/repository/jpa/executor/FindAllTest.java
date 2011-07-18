package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;
import org.springframework.test.util.ReflectionTestUtils;

public class FindAllTest {
    private EntityManager entityManager;
    private EntityClassResolver entityClassResolver;
    private Executor executor;

    @Before
    public void setUp() {
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        executor = new FindAll(entityClassResolver);

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    public void findAll() {
        final Object repository = new Object();
        final Object mockEntity = new Object();
        final Method method = null;

        final List<Object> entities = Collections.singletonList(mockEntity);
        final Query query = EasyMock.createMock(Query.class);

        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        entityManager.createQuery("select e from Object e");
        EasyMock.expectLastCall().andReturn(query);
        query.getResultList();
        EasyMock.expectLastCall().andReturn(entities);
        EasyMock.replay(entityClassResolver, entityManager, query);

        Assert.assertEquals(entities, executor.execute(repository, method));
        EasyMock.verify(entityClassResolver, entityManager, query);

    }
}
