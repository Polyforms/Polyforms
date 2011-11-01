package org.polyforms.repository.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.ConditionalExecutor;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Strategy of finding {@link Executor} for specified method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class NameBasedExecutorFinder implements ExecutorFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(NameBasedExecutorFinder.class);
    private final Map<String, Set<ConditionalExecutor>> conditionalExecutors = new HashMap<String, Set<ConditionalExecutor>>();
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
            if (executorAliasHolder.isPrefix(executorName)) {
                LOGGER.info("Add wildcard executor {}.", executorName);
                mapExecutor(wildcardExecutors, executorName, executor);
            } else {
                LOGGER.info("Add executor {}.", executorName);
                mapExecutor(this.executors, executorName, executor);
            }
        }
    }

    private void mapExecutor(final Map<String, Executor> executors, final String name, final Executor executor) {
        for (final String prefix : executorAliasHolder.getAliases(name)) {
            LOGGER.debug("Add alias {} for executor {}.", prefix, name);
            if (executor instanceof ConditionalExecutor) {
                if (!conditionalExecutors.containsKey(prefix)) {
                    conditionalExecutors.put(prefix, new LinkedHashSet<ConditionalExecutor>());
                }
                conditionalExecutors.get(prefix).add((ConditionalExecutor) executor);
            } else {
                executors.put(prefix, executor);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Executor findExecutor(final Method method) {
        final String methodName = method.getName();

        if (conditionalExecutors.containsKey(methodName)) {
            for (final ConditionalExecutor executor : conditionalExecutors.get(methodName)) {
                if (executor.matches(method)) {
                    return executor;
                }
            }
        }

        if (executors.containsKey(methodName)) {
            final Executor executor = executors.get(methodName);
            LOGGER.trace("Hit cache executor {} for method {}.", executor, method);
            return executor;
        }

        final Executor executor = getWildcardExecutor(methodName);
        LOGGER.debug("Found executor {} for method {}.", executor, method);
        return executor;
    }

    private Executor getWildcardExecutor(final String methodName) {
        for (final Entry<String, Executor> entry : wildcardExecutors.entrySet()) {
            if (methodName.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        return Executor.UNSUPPORTED;
    }
}
