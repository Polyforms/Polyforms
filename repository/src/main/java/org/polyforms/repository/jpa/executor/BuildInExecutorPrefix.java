package org.polyforms.repository.jpa.executor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.polyforms.repository.spi.ExecutorPrefix;

/**
 * Implementation of {@link ExecutorPrefix} for build in executors.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class BuildInExecutorPrefix implements ExecutorPrefix {
    private final Map<String, String[]> alias = new HashMap<String, String[]>();

    /**
     * Create a default instance.
     */
    public BuildInExecutorPrefix() {
        alias.put("save", new String[] { "create", "persist" });
        alias.put("update", new String[] { "merge" });
        alias.put("delete", new String[] { "remove" });
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String[]> getPrefix() {
        return Collections.unmodifiableMap(alias);
    }
}
