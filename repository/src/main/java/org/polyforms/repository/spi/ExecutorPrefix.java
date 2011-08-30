package org.polyforms.repository.spi;

import java.util.Map;

/**
 * Interface for getting prefix for executor.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ExecutorPrefix {
    /**
     * Prefix for specified executor name.
     * 
     * @return prefix mapping with specified name
     */
    Map<String, String[]> getPrefix();
}
