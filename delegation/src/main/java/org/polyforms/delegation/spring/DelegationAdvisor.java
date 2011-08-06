package org.polyforms.delegation.spring;

import java.lang.reflect.Method;

import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.aop.DelegationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.aop.Advisor} for methods which have a linked Delegation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class DelegationAdvisor extends DefaultPointcutAdvisor {
    private static final long serialVersionUID = -8805686347009910065L;

    /**
     * Create an default instance with {@link DelegationService}.
     */
    @Autowired
    public DelegationAdvisor(final DelegationService delegationService) {
        super(new StaticMethodMatcherPointcut() {
            /**
             * {@inheritDoc}
             */
            public boolean matches(final Method method, final Class<?> targetClass) {
                return delegationService.supports(targetClass, method);
            }
        }, new DelegationInterceptor(delegationService));
    }
}
