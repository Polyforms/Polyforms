package org.polyforms.repository.jpa.querydsl;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.ConditionalExecutor;
import org.polyforms.repository.spi.EntityClassResolver;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;

public class QueryDslExecutorTest {
    private EntityClassResolver entityClassResolver;
    private ConditionalExecutor executor;

    @Before
    public void setUp() {
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        executor = new QueryDslExecutor(entityClassResolver) {
            @Override
            protected Object getResult(final EntityPath<?> entityPath, final Method method, final Object... arguments) {
                return 2L;
            }
        };
    }

    @Test
    public void execute() {
        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(MockEntity.class);
        EasyMock.replay(entityClassResolver);

        Assert.assertEquals(2L, executor.execute(new Object(), null, new Object[0]));
        EasyMock.verify(entityClassResolver);
    }

    @Test
    public void matches() throws NoSuchMethodException {
        Assert.assertTrue(executor.matches(MockRepository.class.getMethod("get", new Class<?>[] { Predicate.class })));
    }

    @Test
    public void unmatches() throws NoSuchMethodException {
        Assert.assertFalse(executor.matches(MockRepository.class.getMethod("get", new Class<?>[] { String.class })));
    }

    @Test
    public void unmatchesEmptyParameter() throws NoSuchMethodException {
        Assert.assertFalse(executor.matches(MockRepository.class.getMethod("get", new Class<?>[0])));
    }

    private interface MockRepository {
        MockEntity get(Predicate predicate);

        MockEntity get();

        MockEntity get(String code);
    }
}
