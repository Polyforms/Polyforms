package org.polyforms.event.spring;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.event.NoOperation;
import org.polyforms.event.aop.NoOperationInterceptor;
import org.springframework.aop.MethodMatcher;

public class NoOperationAdvisorTest {
    private final NoOperationAdvisor noOperationAdvisor = new NoOperationAdvisor();

    @Test
    public void advice() {
        Assert.assertTrue(noOperationAdvisor.getAdvice() instanceof NoOperationInterceptor);
    }

    @Test
    public void matches() throws NoSuchMethodException {
        final MethodMatcher methodMatcher = noOperationAdvisor.getPointcut().getMethodMatcher();
        Assert.assertTrue(methodMatcher.matches(MockInterface.class.getMethod("noOperation", new Class<?>[0]),
                MockInterface.class));
    }

    @Test
    public void notMatches() throws NoSuchMethodException {
        final MethodMatcher methodMatcher = noOperationAdvisor.getPointcut().getMethodMatcher();
        Assert.assertFalse(methodMatcher.matches(MockInterface.class.getMethod("normal", new Class<?>[0]),
                MockInterface.class));
    }

    public interface MockInterface {
        @NoOperation
        void noOperation();

        void normal();
    }
}
