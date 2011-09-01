package org.polyforms.repository.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link MethodInterceptor} implementation for methods in Repository.
 * 
 * The interceptor finds corresponding {@link Executor} and executes it.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class RepositoryInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryInterceptor.class);
    private final Map<Method, Executor> matchedExecutorCache = new HashMap<Method, Executor>();
    private final ExecutorFinder executorFinder;

    /**
     * Create an instance with {@link ExecutorFinder}.
     */
    @Inject
    public RepositoryInterceptor(final ExecutorFinder executorFinder) {
        this.executorFinder = executorFinder;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(final MethodInvocation invocation) {
        final Method method = invocation.getMethod();
        final Executor executor = findExecutorWithCache(method);
        return executor.execute(invocation.getThis(), method, invocation.getArguments());
    }

    private Executor findExecutorWithCache(final Method method) {
        if (!matchedExecutorCache.containsKey(method)) {
            LOGGER.trace("Cache missed when finding executor for method {}.", method);
            final Executor executor = executorFinder.findExecutor(method);
            matchedExecutorCache.put(method, executor);
        }

        final Executor executor = matchedExecutorCache.get(method);
        LOGGER.debug("Found executor {} for method {}.", executor, method);
        return executor;
    }
}
