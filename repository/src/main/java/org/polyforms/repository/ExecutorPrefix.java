package org.polyforms.repository;

import java.util.Set;

public interface ExecutorPrefix {
    Set<String> getPrefix(final String name);

    boolean isWildcard(final String name);
}
