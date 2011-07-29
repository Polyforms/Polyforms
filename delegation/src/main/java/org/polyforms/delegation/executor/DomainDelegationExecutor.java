package org.polyforms.delegation.executor;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.springframework.core.convert.ConversionService;

/**
 * The {@link org.polyforms.delegation.support.DelegationExecutor} which delegate a method to first argument of it.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
final class DomainDelegationExecutor extends AbstactDelegationExecutor {
    protected DomainDelegationExecutor(final ConversionService conversionService) {
        super(conversionService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getTarget(final Delegation delegation, final Object[] arguments) {
        if (arguments.length == 0) {
            throw new IllegalArgumentException("There is no parameters in method["
                    + delegation.getDelegatee().getName() + "]. ");
        }
        final Object argument = arguments[0];
        if (argument == null) {
            throw new IllegalArgumentException("The first argument of invocation of method["
                    + delegation.getDelegatee().getName() + "] is null.");
        }
        return getConversionService().convert(argument, delegation.getDelegatee().getDeclaringClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[] tailorArguments(final Object[] arguments) {
        final Object[] tailoredArguments = new Object[arguments.length - 1];
        System.arraycopy(arguments, 1, tailoredArguments, 0, arguments.length - 1);
        return tailoredArguments;
    }
}
