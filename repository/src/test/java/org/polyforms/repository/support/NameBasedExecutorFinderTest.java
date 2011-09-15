package org.polyforms.repository.support;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.ConditionalExecutor;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorFinder;

public class NameBasedExecutorFinderTest {
    private final Executor get = new Get();
    private final Executor getBy = new GetBy();
    private final Find find = new Find();
    private ExecutorPrefixHolder executorPrefixHolder;
    private ExecutorFinder executorFinder;
    private Set<String> executorPrefix;

    @Before
    public void setUp() {
        executorPrefix = new HashSet<String>();
        executorPrefix.add("get");
        executorPrefix.add("load");
        executorPrefix.add("find");

        final Set<Executor> executors = new HashSet<Executor>();
        executors.add(get);
        executors.add(getBy);
        executors.add(find);

        executorPrefixHolder = EasyMock.createMock(ExecutorPrefixHolder.class);
        executorPrefixHolder.isPrefix("Get");
        EasyMock.expectLastCall().andReturn(false);
        executorPrefixHolder.getAliases("Get");
        EasyMock.expectLastCall().andReturn(executorPrefix);
        executorPrefixHolder.isPrefix("GetBy");
        EasyMock.expectLastCall().andReturn(true);
        executorPrefixHolder.getAliases("GetBy");
        EasyMock.expectLastCall().andReturn(executorPrefix);
        executorPrefixHolder.isPrefix("Find");
        EasyMock.expectLastCall().andReturn(false);
        executorPrefixHolder.getAliases("Find");
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
    public void findConditionalExecutor() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("find", new Class<?>[0]);
        Assert.assertSame(find, executorFinder.findExecutor(method));
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

    private static class Find implements ConditionalExecutor {
        public Object execute(final Object target, final Method method, final Object... arguments) {
            return null;
        }

        public boolean matches(final Method method) {
            return method.getName().equals("find");
        }
    }

    private static interface Repository {
        Object get();

        Object load();

        Object find();

        Object getByName();
    }
}
