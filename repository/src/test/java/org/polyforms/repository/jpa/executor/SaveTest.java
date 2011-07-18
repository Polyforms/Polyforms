package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.Executor;
import org.springframework.test.util.ReflectionTestUtils;

public class SaveTest {
    private final Object repository = new Object();
    private EntityManager entityManager;
    private Executor executor;

    @Before
    public void setUp() {
        executor = new Save();

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    public void save() throws NoSuchMethodException {
        final Object repository = new Object();
        final Object mockEntity = new Object();
        final Method method = Repository.class.getMethod("save", new Class<?>[] { Object.class });

        entityManager.persist(mockEntity);
        EasyMock.replay(entityManager);

        executor.execute(repository, method, new Object[] { mockEntity });
        EasyMock.verify(entityManager);
    }

    @Test
    public void saveWithVarArgs() throws NoSuchMethodException {
        final Object mockEntity = new Object();
        final Method method = Repository.class.getMethod("save", new Class<?>[] { Object[].class });

        entityManager.persist(mockEntity);
        EasyMock.replay(entityManager);

        executor.execute(repository, method, new Object[] { new Object[] { mockEntity } });
        EasyMock.verify(entityManager);
    }

    @Test
    public void saveWithoutArguments() {
        executor.execute(repository, null, new Object[0]);
    }

    private static interface Repository {
        void save(Object entity);

        void save(Object... entity);
    }
}
