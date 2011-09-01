package org.polyforms.repository.spi;

import java.util.Map;

/**
 * Interface for getting alias for executor prefix.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ExecutorPrefixAlias {
    /**
     * Alias for specified executor prefix.
     * 
     * @return alias mapping with specified prefix
     */
    Map<String, String[]> getAliases();
}
