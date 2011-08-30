package org.polyforms.repository.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.ExecutorPrefix;

@Named
public class SimpleExecutorPrefixHolder implements ExecutorPrefixHolder {
    private static final String WILDCARD_SUFFIX = "By";
    private final Map<String, Set<String>> prefix = new HashMap<String, Set<String>>();

    @Inject
    public SimpleExecutorPrefixHolder(final Set<ExecutorPrefix> executorPrefixes) {
        for (final ExecutorPrefix executorPrefix : executorPrefixes) {
            final Map<String, String[]> partPrefix = executorPrefix.getPrefix();
            for (final String name : partPrefix.keySet()) {
                if (!prefix.containsKey(name)) {
                    addExecutorPrefix(name);
                }
                prefix.get(name).addAll(asSet(partPrefix.get(name)));
            }
        }
    }

    private void addExecutorPrefix(final String name) {
        final HashSet<String> prefixSet = new HashSet<String>();
        prefixSet.add(name);
        prefix.put(name, prefixSet);
    }

    private Set<String> asSet(final String[] alias) {
        return new HashSet<String>(Arrays.asList(alias));
    }

    public Set<String> getPrefix(final String name) {
        final String executorName = isWildcard(name) ? name.substring(0, name.length() - WILDCARD_SUFFIX.length())
                : name;
        if (!prefix.containsKey(executorName)) {
            addExecutorPrefix(executorName);
        }
        return Collections.unmodifiableSet(prefix.get(executorName));

    }

    public boolean isWildcard(final String name) {
        return name.endsWith(WILDCARD_SUFFIX);
    }
}
