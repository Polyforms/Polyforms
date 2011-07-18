package org.polyforms.repository.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorAlias;
import org.polyforms.repository.spi.ExecutorFinder;
import org.springframework.util.StringUtils;

/**
 * Strategy of finding {@link Executor} inheriting {@link NameBasedExecutor} for specific method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class NameBasedExecutorFinder implements ExecutorFinder {
    private static final String WILDCARD_SUFFIX = "By";
    private final Map<String, Executor> executors = new HashMap<String, Executor>();
    private final Map<String, Executor> wildcardExecutor = new HashMap<String, Executor>();
    private final Map<String, Executor> matchedExecutorCache = new HashMap<String, Executor>();
    private final Set<ExecutorAlias> executorAliases;

    /**
     * Create an instance with {@link Executor}s.
     */
    @Inject
    public NameBasedExecutorFinder(final Set<Executor> executors, final Set<ExecutorAlias> executorAliases) {
        this.executorAliases = executorAliases;

        for (final Executor executor : executors) {
            final String name = StringUtils.uncapitalize(executor.getClass().getSimpleName());
            if (name.endsWith(WILDCARD_SUFFIX)) {
                mapExecutor(wildcardExecutor, name.substring(0, name.length() - WILDCARD_SUFFIX.length()), executor);
            } else {
                mapExecutor(this.executors, name, executor);
            }
        }
    }

    private void mapExecutor(final Map<String, Executor> executors, final String name, final Executor executor) {
        executors.put(name, executor);
        for (final String alias : getAlias(name)) {
            executors.put(alias, executor);
        }
    }

    private Set<String> getAlias(final String name) {
        final Set<String> alias = new HashSet<String>();
        for (final ExecutorAlias executorAlias : executorAliases) {
            alias.addAll(executorAlias.alias(name));
        }

        return alias;
    }

    /**
     * {@inheritDoc}
     */
    public Executor getExecutor(final Method method) {
        final String methodName = method.getName();

        if (executors.containsKey(methodName)) {
            return executors.get(methodName);
        }

        return getWildcardExecutorWithCache(methodName);
    }

    private Executor getWildcardExecutorWithCache(final String methodName) {
        if (!matchedExecutorCache.containsKey(methodName)) {
            matchedExecutorCache.put(methodName, getWildcardExecutor(methodName));
        }

        return matchedExecutorCache.get(methodName);
    }

    private Executor getWildcardExecutor(final String methodName) {
        for (final Entry<String, Executor> entry : wildcardExecutor.entrySet()) {
            if (methodName.startsWith(entry.getKey()) && methodName.length() > entry.getKey().length()) {
                return entry.getValue();
            }
        }

        return Executor.UNSUPPORTED;
    }
}
