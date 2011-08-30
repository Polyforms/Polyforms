package org.polyforms.repository.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.ExecutorAliasHolder;
import org.polyforms.repository.spi.ExecutorAlias;

@Named
public class SimpleExecutorAliasHolder implements ExecutorAliasHolder {
    private final Map<String, Set<String>> alias = new HashMap<String, Set<String>>();

    @Inject
    public SimpleExecutorAliasHolder(final Set<ExecutorAlias> executorAliases) {
        for (ExecutorAlias executorAlias : executorAliases) {
            Map<String, String[]> partAlias = executorAlias.getAlias();
            for (String name : partAlias.keySet()) {
                if (!alias.containsKey(name)) {
                    alias.put(name, new HashSet<String>());
                }
                alias.get(name).addAll(asSet(partAlias.get(name)));
            }
        }
    }

    private Set<String> asSet(final String[] alias) {
        return new HashSet<String>(Arrays.asList(alias));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.polyforms.repository.support.ExecutorAliasHolder#getAlias(java.lang.String)
     */
    public Set<String> getAlias(final String name) {
        if (alias.containsKey(name)) {
            return Collections.unmodifiableSet(alias.get(name));
        } else {
            return Collections.emptySet();
        }
    }
}
