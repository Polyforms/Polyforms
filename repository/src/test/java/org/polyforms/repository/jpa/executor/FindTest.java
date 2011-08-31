package org.polyforms.repository.jpa.executor;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.EntityHelper;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;
import org.springframework.test.util.ReflectionTestUtils;

public class FindTest {
    private final Object repository = new Object();

    private EntityManager entityManager;
    private EntityHelper entityHelper;
    private EntityClassResolver entityClassResolver;
    private Executor executor;

    @Before
    public void setUp() {
        entityHelper = EasyMock.createMock(EntityHelper.class);
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        executor = new Find(entityHelper, entityClassResolver);

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    public void find() throws NoSuchMethodException {
        final Object mockEntity = new Object();

        final List<Object> entities = Collections.singletonList(mockEntity);
        final Query query = EasyMock.createMock(Query.class);

        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        entityHelper.getIdentifierName(Object.class);
        EasyMock.expectLastCall().andReturn("id");
        entityManager.createQuery("select e from Object e where e.id in :identifiers");
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter(EasyMock.eq("identifiers"), EasyMock.isA(List.class));
        EasyMock.expectLastCall().andReturn(query);
        query.getResultList();
        EasyMock.expectLastCall().andReturn(entities);
        EasyMock.replay(entityHelper, entityClassResolver, entityManager, query);

        Assert.assertEquals(entities, executor.execute(repository,
                Repository.class.getMethod("find", new Class<?>[] { Object[].class }),
                new Object[] { new Long[] { 1L } }));
        EasyMock.verify(entityHelper, entityClassResolver, entityManager, query);
    }

    @Test
    public void get() throws NoSuchMethodException {
        final Object mockEntity = new Object();

        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        entityManager.find(Object.class, 1L);
        EasyMock.expectLastCall().andReturn(mockEntity);
        EasyMock.replay(entityClassResolver, entityManager);

        Assert.assertEquals(mockEntity,
                executor.execute(repository, Repository.class.getMethod("find", new Class<?>[] { Object.class }), 1L));
        EasyMock.verify(entityClassResolver, entityManager);
    }

    @Test
    public void findWithoutArguments() throws NoSuchMethodException {
        Assert.assertEquals(Collections.EMPTY_LIST, executor.execute(repository,
                Repository.class.getMethod("find", new Class<?>[] { Object[].class }), new Object[0]));
    }

    @Test
    public void getWithoutArguments() throws NoSuchMethodException {
        Assert.assertNull(executor.execute(repository,
                Repository.class.getMethod("find", new Class<?>[] { Object.class }), new Object[0]));
    }

    private static interface Repository {
        Object find(Object id);

        List<Object> find(Object... ids);
    }
}
