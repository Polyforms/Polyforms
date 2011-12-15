package org.polyforms.repository.jpa.executor;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;
import org.springframework.test.util.ReflectionTestUtils;

public class LoadTest {
    private final Object repository = new Object();

    private EntityManager entityManager;
    private EntityClassResolver entityClassResolver;
    private Executor executor;

    @Before
    public void setUp() {
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        executor = new Load(entityClassResolver);

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    public void load() throws NoSuchMethodException {
        final Object mockEntity = new Object();

        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        entityManager.find(Object.class, 1L);
        EasyMock.expectLastCall().andReturn(mockEntity);
        EasyMock.replay(entityClassResolver, entityManager);

        Assert.assertEquals(mockEntity,
                executor.execute(repository, Repository.class.getMethod("load", new Class<?>[] { Object.class }), 1L));
        EasyMock.verify(entityClassResolver, entityManager);
    }

    @Test(expected = EntityNotFoundException.class)
    public void entityNotFound() throws NoSuchMethodException {
        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        entityManager.find(Object.class, 1L);
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(entityClassResolver, entityManager);

        executor.execute(repository, Repository.class.getMethod("load", new Class<?>[] { Object.class }), 1L);
        EasyMock.verify(entityClassResolver, entityManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadWithoutArguments() throws NoSuchMethodException {
        executor.execute(repository, Repository.class.getMethod("load", new Class<?>[] { Object.class }), new Object[0]);
    }

    private static interface Repository {
        Object load(Object id);
    }
}
