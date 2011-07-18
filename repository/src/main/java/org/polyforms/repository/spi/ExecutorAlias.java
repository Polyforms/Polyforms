package org.polyforms.repository.spi;

import java.util.Set;

public interface ExecutorAlias {
    Set<String> alias(String name);
}
