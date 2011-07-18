package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.Executor;
import org.springframework.test.util.ReflectionTestUtils;

public class RemoveTest {
    private final Object repository = new Object();

    private EntityManager entityManager;
    private Executor executor;

    @Before
    public void setUp() {
        executor = new Remove();

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    public void remove() throws NoSuchMethodException {
        final Object mockEntity = new Object();
        final Method method = Repository.class.getMethod("remove", new Class<?>[] { Object.class });

        entityManager.remove(mockEntity);
        EasyMock.replay(entityManager);

        executor.execute(repository, method, new Object[] { mockEntity });
        EasyMock.verify(entityManager);
    }

    @Test
    public void removeWithVarArgs() throws NoSuchMethodException {
        final Object repository = new Object();
        final Object mockEntity = new Object();
        final Method method = Repository.class.getMethod("remove", new Class<?>[] { Object[].class });

        entityManager.remove(mockEntity);
        EasyMock.replay(entityManager);

        executor.execute(repository, method, new Object[] { new Object[] { mockEntity } });
        EasyMock.verify(entityManager);
    }

    @Test
    public void removeWithoutArguments() {
        executor.execute(repository, null, new Object[0]);
    }

    private static interface Repository {
        void remove(Object entity);

        void remove(Object... entity);
    }
}
