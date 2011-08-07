package org.polyforms.delegation.executor;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.di.BeanContainer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

/**
 * The {@link org.polyforms.delegation.support.DelegationExecutor} which delegate a method to a bean in Ioc container.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
final class BeanDelegationExecutor extends AbstactDelegationExecutor {
    private final BeanContainer beanContainer;

    protected BeanDelegationExecutor(final ConversionService conversionService, final BeanContainer beanContainer) {
        super(conversionService);
        this.beanContainer = beanContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getTarget(final Delegation delegation, final Object[] arguments) {
        if (StringUtils.hasText(delegation.getDelegateeName())) {
            return beanContainer.getBean(delegation.getDelegateeName(), delegation.getDelegateeType());
        }

        return beanContainer.getBean(delegation.getDelegateeType());
    }
}
