package org.polyforms.repository.integration.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.polyforms.repository.spi.ExecutorPrefixAlias;
import org.springframework.stereotype.Component;

@Component
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
