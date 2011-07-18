package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;
import org.springframework.test.util.ReflectionTestUtils;

public class GetTest {
    private final Object repository = new Object();
    private final Object mockEntity = new Object();
    private final Method method = null;

    private EntityManager entityManager;
    private EntityClassResolver entityClassResolver;
    private Executor executor;

    @Before
    public void setUp() {
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        executor = new Get(entityClassResolver);

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    public void getWithoutArgument() {
        Assert.assertNull(executor.execute(repository, method));
    }

    @Test
    public void get() {
        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        entityManager.find(Object.class, 1L);
        EasyMock.expectLastCall().andReturn(mockEntity);
        EasyMock.replay(entityClassResolver, entityManager);

        Assert.assertEquals(mockEntity, executor.execute(repository, method, 1L));
        EasyMock.verify(entityClassResolver, entityManager);
    }
}
