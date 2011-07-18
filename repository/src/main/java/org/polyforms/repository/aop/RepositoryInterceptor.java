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

/**
 * Interceptor implementing methods in Repository.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class RepositoryInterceptor implements MethodInterceptor {
    private final Map<Method, Executor> matchedExecutors = new HashMap<Method, Executor>();
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
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Method method = invocation.getMethod();
        final Executor executor = getExecutorWithCache(method);
        return executor.execute(invocation.getThis(), method, invocation.getArguments());
    }

    private Executor getExecutorWithCache(final Method method) {
        if (matchedExecutors.containsKey(method)) {
            return matchedExecutors.get(method);
        }

        final Executor executor = executorFinder.getExecutor(method);
        matchedExecutors.put(method, executor);
        return executor;
    }
}
