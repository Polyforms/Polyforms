package org.polyforms.repository.spi;

import java.util.Map;

/**
 * Interface for getting alias for executor's name.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ExecutorAlias {
    /**
     * Alias for specified executor name.
     * 
     * @return alias mapping with specified name
     */
    Map<String, String[]> getAlias();
}
