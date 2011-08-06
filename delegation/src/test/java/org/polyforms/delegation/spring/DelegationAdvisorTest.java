package org.polyforms.delegation.spring;

import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.aop.DelegationInterceptor;
import org.springframework.aop.MethodMatcher;

public class DelegationAdvisorTest {
    private DelegationService delegationService;
    private DelegationAdvisor delegationAdvisor;

    @Before
    public void setUp() {
        delegationService = EasyMock.createMock(DelegationService.class);
        delegationAdvisor = new DelegationAdvisor(delegationService);
    }

    @Test
    public void advice() {
        Assert.assertTrue(delegationAdvisor.getAdvice() instanceof DelegationInterceptor);
    }

    @Test
    public void matches() throws NoSuchMethodException {
        final Method method = String.class.getMethod("toString", new Class<?>[0]);
        delegationService.supports(String.class, method);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(delegationService);

        final MethodMatcher methodMatcher = delegationAdvisor.getPointcut().getMethodMatcher();
        Assert.assertTrue(methodMatcher.matches(method, String.class));
        EasyMock.verify(delegationService);
    }
}
