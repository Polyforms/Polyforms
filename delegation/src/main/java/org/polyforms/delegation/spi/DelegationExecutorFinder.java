package org.polyforms.delegation.spi;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;

/**
 * Strategy of finding executor to execute specific delegation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationExecutorFinder {
    /**
     * Get matched executor to execute specific delegation.
     * 
     * @param delegation to be executed by {@link DelegationExecutor}
     * 
     * @return matched executor
     */
    DelegationExecutor getDelegationExecutor(Delegation delegation);
}
