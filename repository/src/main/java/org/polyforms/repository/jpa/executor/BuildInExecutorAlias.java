package org.polyforms.repository.jpa.executor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.polyforms.repository.spi.ExecutorAlias;

/**
 * Implementation of {@link ExecutorAlias} for build in executors.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class BuildInExecutorAlias implements ExecutorAlias {
    private final Map<String, Set<String>> alias = new HashMap<String, Set<String>>();

    /**
     * Create a default instance.
     */
    public BuildInExecutorAlias() {
        alias.put("save", asSet(new String[] { "create", "persist" }));
        alias.put("remove", asSet(new String[] { "delete" }));
        alias.put("update", asSet(new String[] { "delete", "remove" }));
    }

    private Set<String> asSet(final String[] alias) {
        return new HashSet<String>(Arrays.asList(alias));
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getAlias(final String name) {
        if (!alias.containsKey(name)) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableSet(alias.get(name));
    }
}
