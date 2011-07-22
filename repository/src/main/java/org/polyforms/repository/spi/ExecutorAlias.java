package org.polyforms.repository.spi;

import java.util.Set;

/**
 * Interface for getting alias for executor's name.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ExecutorAlias {
    /**
     * Get alias for specified name.
     * 
     * @param name of executor
     * @return alias for specified name or empty set if no matching
     */
    Set<String> alias(String name);
}
