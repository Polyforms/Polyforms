package org.polyforms.repository.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorAlias;
import org.polyforms.repository.spi.ExecutorFinder;

public class NameBasedExecutorFinderTest {
    private final Executor get = new Get();
    private final Executor getBy = new GetBy();
    @SuppressWarnings("unchecked")
    private final ExecutorAlias executorAlias = new ExecutorAlias() {
        private final HashSet<String> alias = new HashSet<String>(Arrays.asList(new String[] { "load" }));

        public Set<String> alias(final String name) {
            return name.equals("get") ? alias : Collections.EMPTY_SET;
        }
    };
    private ExecutorFinder executorFinder;

    @Before
    public void setUp() {
        final Set<Executor> executors = new HashSet<Executor>();
        executors.add(get);
        executors.add(getBy);
        executorFinder = new NameBasedExecutorFinder(executors, Collections.singleton(executorAlias));
    }

    @Test
    public void getExecutor() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("get", new Class<?>[0]);
        Assert.assertSame(get, executorFinder.getExecutor(method));
    }

    @Test
    public void getExecutorWithAlias() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("load", new Class<?>[0]);
        Assert.assertSame(get, executorFinder.getExecutor(method));
    }

    @Test
    public void getWildcardExecutor() throws NoSuchMethodException {
        final Method method = Repository.class.getMethod("getByName", new Class<?>[0]);
        Assert.assertSame(getBy, executorFinder.getExecutor(method));
        // Just for testing cache.
        Assert.assertSame(getBy, executorFinder.getExecutor(method));
    }

    @Test
    public void unsupportedByExecutor() throws NoSuchMethodException {
        final Method method = String.class.getMethod("toString", new Class<?>[0]);
        Assert.assertSame(Executor.UNSUPPORTED, executorFinder.getExecutor(method));
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
