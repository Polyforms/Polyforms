package org.polyforms.repository.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.spi.ExecutorPrefixAlias;
import org.springframework.util.StringUtils;

@Named
public class SimpleExecutorPrefix implements ExecutorPrefix {
    private static final String WILDCARD_SUFFIX = "By";
    private final Map<String, Set<String>> prefix = new HashMap<String, Set<String>>();

    @Inject
    public SimpleExecutorPrefix(final Set<ExecutorPrefixAlias> executorAliases) {
        for (final ExecutorPrefixAlias executorAlias : executorAliases) {
            final Map<String, String[]> partAlias = executorAlias.getAlias();
            for (final Entry<String, String[]> entry : partAlias.entrySet()) {
                final String name = entry.getKey();
                if (!prefix.containsKey(name)) {
                    addExecutorPrefix(name);
                }
                for (final String alias : entry.getValue()) {
                    prefix.get(name).add(alias);
                }
            }
        }
    }

    private void addExecutorPrefix(final String name) {
        final HashSet<String> prefixSet = new HashSet<String>();
        prefixSet.add(name);
        prefix.put(name, prefixSet);
    }

    public Set<String> getPrefix(final String name) {
        final String executorName = convertToPrefix(name);
        if (!prefix.containsKey(executorName)) {
            addExecutorPrefix(executorName);
        }
        return Collections.unmodifiableSet(prefix.get(executorName));

    }

    public boolean isWildcard(final String name) {
        return name.endsWith(WILDCARD_SUFFIX);
    }

    public String convertToPrefix(final String name) {
        return StringUtils.uncapitalize(isWildcard(name) ? name.substring(0, name.length() - WILDCARD_SUFFIX.length())
                : name);
    }
}
