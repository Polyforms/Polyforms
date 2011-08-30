package org.polyforms.repository.support;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorFinder;

public class NameBasedExecutorFinderTest {
    private final Executor get = new Get();
    private final Executor getBy = new GetBy();
    private ExecutorPrefix executorPrefixHolder;
    private ExecutorFinder executorFinder;
    private Set<String> executorPrefix;

    @Before
    public void setUp() {
        executorPrefix = new HashSet<String>();
        executorPrefix.add("get");
        executorPrefix.add("load");

        final Set<Executor> executors = new HashSet<Executor>();
        executors.add(get);
        executors.add(getBy);

        executorPrefixHolder = EasyMock.createMock(ExecutorPrefix.class);
        executorPrefixHolder.isWildcard("get");
        EasyMock.expectLastCall().andReturn(false);
        executorPrefixHolder.getPrefix("get");
        EasyMock.expectLastCall().andReturn(executorPrefix);
        executorPrefixHolder.isWildcard("getBy");
        EasyMock.expectLastCall().andReturn(true);
        executorPrefixHolder.getPrefix("getBy");
        EasyMock.expectLastCall().andReturn(executorPrefix);
        EasyMock.replay(executorPrefixHolder);

        executorFinder = new NameBasedExecutorFinder(executors, executorPrefixHolder);
    }

    @After
    public void tearDown() {
        EasyMock.verify(executorPrefixHolder);
    }

    @Test
    public void findExecutor() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("get", new Class<?>[0]);
        Assert.assertSame(get, executorFinder.findExecutor(method));
    }

    @Test
    public void findExecutorWithAlias() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("load", new Class<?>[0]);
        Assert.assertSame(get, executorFinder.findExecutor(method));
    }

    @Test
    public void getWildcardExecutor() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("getByName", new Class<?>[0]);
        Assert.assertSame(getBy, executorFinder.findExecutor(method));
    }

    @Test
    public void unsupportedByExecutor() throws NoSuchMethodException {
        final Method method = String.class.getMethod("toString", new Class<?>[0]);
        Assert.assertSame(Executor.UNSUPPORTED, executorFinder.findExecutor(method));
    }

    private static class Get implements Executor {
        public Object execute(final Object target, final Method method, final Object... arguments) {
            return null;
        }
    }

    private static class GetBy implements Executor {
        public Object execute(final Object target, final Method method, final Object... arguments) {
            return null;
        }
    }

    private static interface Repository {
        Object get();

        Object load();

        Object getByName();
    }
}
