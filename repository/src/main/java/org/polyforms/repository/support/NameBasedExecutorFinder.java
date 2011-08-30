package org.polyforms.repository.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Strategy of finding {@link Executor} for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class NameBasedExecutorFinder implements ExecutorFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(NameBasedExecutorFinder.class);
    private final Map<String, Executor> executors = new HashMap<String, Executor>();
    private final Map<String, Executor> wildcardExecutors = new HashMap<String, Executor>();
    private final ExecutorPrefixHolder executorAliasHolder;

    /**
     * Create an instance with {@link Executor}s.
     */
    @Inject
    public NameBasedExecutorFinder(final Set<Executor> executors, final ExecutorPrefixHolder executorAliasHolder) {
        this.executorAliasHolder = executorAliasHolder;

        for (final Executor executor : executors) {
            final String executorName = executor.getClass().getSimpleName();
            final String name = StringUtils.uncapitalize(executorName);
            if (executorAliasHolder.isWildcard(name)) {
                LOGGER.info("Add wildcard executor {}.", executorName);
                mapExecutor(wildcardExecutors, name, executor);
            } else {
                LOGGER.info("Add executor {}.", executorName);
                mapExecutor(this.executors, name, executor);
            }
        }
    }

    private void mapExecutor(final Map<String, Executor> executors, final String name, final Executor executor) {
        for (final String prefix : executorAliasHolder.getPrefix(name)) {
            LOGGER.debug("Add alias {} for executor {}.", prefix, name);
            executors.put(prefix, executor);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Executor findExecutor(final Method method) {
        final String methodName = method.getName();

        if (executors.containsKey(methodName)) {
            final Executor executor = executors.get(methodName);
            LOGGER.trace("Found executor {} for method {}.", executor, method);
            return executor;
        }

        final Executor executor = getWildcardExecutor(methodName);
        LOGGER.debug("Found executor {} for method {}.", executor, method);
        return executor;
    }

    private Executor getWildcardExecutor(final String methodName) {
        for (final Entry<String, Executor> entry : wildcardExecutors.entrySet()) {
            if (methodName.startsWith(entry.getKey()) && methodName.length() > entry.getKey().length()) {
                return entry.getValue();
            }
        }

        return Executor.UNSUPPORTED;
    }
}
