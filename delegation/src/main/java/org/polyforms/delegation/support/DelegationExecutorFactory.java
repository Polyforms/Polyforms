package org.polyforms.delegation.support;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.delegation.spi.DelegationExecutor;
import org.polyforms.delegation.spi.DelegationExecutorFinder;
import org.polyforms.di.BeanContainer;
import org.springframework.core.convert.ConversionService;

/**
 * Factory which selects executor to execute specific {@link Delegation}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
final class DelegationExecutorFactory implements DelegationExecutorFinder {
    private final Map<Delegation, DelegationExecutor> delegationExecutorMapping = new HashMap<Delegation, DelegationExecutor>();
    private final BeanContainer beanContainer;
    private final DelegationExecutor beanExecutor;
    private final DelegationExecutor domainExecutor;

    @Inject
    public DelegationExecutorFactory(final ConversionService conversionService, final BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
        beanExecutor = new BeanDelegationExecutor(conversionService, beanContainer);
        domainExecutor = new DomainDelegationExecutor(conversionService);
    }

    public DelegationExecutor getDelegationExecutor(final Delegation delegation) {
        if (!delegationExecutorMapping.containsKey(delegation)) {
            delegationExecutorMapping.put(delegation, isBeanDelegation(delegation) ? beanExecutor : domainExecutor);
        }
        return delegationExecutorMapping.get(delegation);
    }

    private boolean isBeanDelegation(final Delegation delegation) {
        return delegation.hasName() || beanContainer.containsBean(delegation.getDelegatee().getDeclaringClass());
    }
}
