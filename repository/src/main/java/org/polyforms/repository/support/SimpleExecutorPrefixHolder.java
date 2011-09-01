package org.polyforms.repository.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.ExecutorPrefixAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link ExecutorPrefixHolder} holds all aliases of prefix of executors.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class SimpleExecutorPrefixHolder implements ExecutorPrefixHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleExecutorPrefixHolder.class);
    private static final String WILDCARD_SUFFIX = "By";
    private final Map<String, Set<String>> prefixAliases = new HashMap<String, Set<String>>();

    /**
     * Create an instance with all {@link ExecutorPrefixAlias}.
     */
    @Inject
    public SimpleExecutorPrefixHolder(final Set<ExecutorPrefixAlias> executorAliases) {
        for (final ExecutorPrefixAlias executorAlias : executorAliases) {
            for (final Entry<String, String[]> entry : executorAlias.getAliases().entrySet()) {
                final String prefix = entry.getKey();
                final String[] aliases = entry.getValue();
                LOGGER.info("Add alias {} for prefix {}.", aliases, prefix);

                if (!prefixAliases.containsKey(prefix)) {
                    addExecutorPrefix(prefix);
                }
                for (final String alias : aliases) {
                    prefixAliases.get(prefix).add(alias);
                }
            }
        }
    }

    private void addExecutorPrefix(final String prefix) {
        final HashSet<String> prefixSet = new HashSet<String>();
        prefixSet.add(prefix);
        prefixAliases.put(prefix, prefixSet);
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getAliases(final String prefix) {
        final String executorPrefix = StringUtils.uncapitalize(isPrefix(prefix) ? prefix.substring(0, prefix.length()
                - WILDCARD_SUFFIX.length()) : prefix);
        if (!prefixAliases.containsKey(executorPrefix)) {
            LOGGER.debug("Prefix {} is not found.", prefix);
            addExecutorPrefix(executorPrefix);
        }
        return Collections.unmodifiableSet(prefixAliases.get(executorPrefix));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPrefix(final String string) {
        return string.endsWith(WILDCARD_SUFFIX);
    }

    /**
     * {@inheritDoc}
     */
    public String removePrefixIfAvailable(final String string) {
        for (final Set<String> aliases : prefixAliases.values()) {
            for (final String alias : aliases) {
                if (string.startsWith(alias)) {
                    LOGGER.debug("Remove prefix {} from {}.", alias, string);
                    return string.substring(alias.length());
                }
            }
        }
        return string;
    }
}

/**
 * Implementation of {@link ExecutorPrefixAlias} returns no aliases.
 * 
 * This is used to instant {@link SimpleExecutorPrefixHolder} if there is no any implementation of
 * {@link ExecutorPrefixAlias}.
 */
@Named
class EmptyExecutorPrefixAlias implements ExecutorPrefixAlias {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, String[]> getAliases() {
        return Collections.EMPTY_MAP;
    }
}
