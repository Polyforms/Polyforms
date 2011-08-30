package org.polyforms.repository.jpa.executor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private final Map<String, String[]> alias = new HashMap<String, String[]>();

    /**
     * Create a default instance.
     */
    public BuildInExecutorAlias() {
        alias.put("save", new String[] { "create", "persist" });
        alias.put("update", new String[] { "merge" });
        alias.put("delete", new String[] { "remove" });
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String[]> getAlias() {
        return Collections.unmodifiableMap(alias);
    }
}
