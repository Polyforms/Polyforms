package org.polyforms.delegation.support;

import java.util.HashMap;
import java.util.Map;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.springframework.core.convert.ConversionService;

/**
 * Factory which selects executor to execute specific {@link Delegation}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
final class DelegationExecutorFactory {
    private final Map<Delegation, DelegationExecutor> delegationExecutorMapping = new HashMap<Delegation, DelegationExecutor>();
    private final BeanContainer beanContainer;
    private final DelegationExecutor beanExecutor;
    private final DelegationExecutor domainExecutor;

    protected DelegationExecutorFactory(final ConversionService conversionService, final BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
        beanExecutor = new BeanDelegationExecutor(conversionService, beanContainer);
        domainExecutor = new DomainDelegationExecutor(conversionService);
    }

    protected DelegationExecutor getDelegationExecutor(final Delegation delegation) {
        if (!delegationExecutorMapping.containsKey(delegation)) {
            delegationExecutorMapping.put(delegation, isBeanDelegation(delegation) ? beanExecutor : domainExecutor);
        }
        return delegationExecutorMapping.get(delegation);
    }

    private boolean isBeanDelegation(final Delegation delegation) {
        return delegation.hasName() || beanContainer.containsBean(delegation.getDelegatee().getDeclaringClass());
    }
}
