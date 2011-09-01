package org.polyforms.repository.integration.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.polyforms.repository.spi.ExecutorPrefixAlias;

/**
 * Implementation of {@link ExecutorPrefixAlias} for build in executors.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class BuildInExecutorPrefixAlias implements ExecutorPrefixAlias {
    private final Map<String, String[]> alias = new HashMap<String, String[]>();

    /**
     * Create a default instance.
     */
    public BuildInExecutorPrefixAlias() {
        alias.put("delete", new String[] { "remove" });
        alias.put("find", new String[] { "get" });
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String[]> getAliases() {
        return Collections.unmodifiableMap(alias);
    }
}
