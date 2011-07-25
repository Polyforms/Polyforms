package org.polyforms.delegation.spi;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;

public interface DelegationExecutorFinder {
    DelegationExecutor getDelegationExecutor(Delegation delegation);
}
