package org.polyforms.repository.aop;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.Executor;
import org.polyforms.repository.spi.ExecutorFinder;

public class RepositoryInterceptorTest {
    private ExecutorFinder executorFinder;
    private RepositoryInterceptor repositoryInterceptor;

    @Before
    public void setUp() {
        executorFinder = EasyMock.createMock(ExecutorFinder.class);
        repositoryInterceptor = new RepositoryInterceptor(executorFinder);
    }

    @Test
    public void invoke() throws Throwable {
        final MethodInvocation invocation = EasyMock.createMock(MethodInvocation.class);
        final Executor executor = EasyMock.createMock(Executor.class);

        final Object target = new Object();
        final Method method = Object.class.getMethod("toString", new Class[0]);
        final Object[] arguments = new Object[0];

        invocation.getMethod();
        EasyMock.expectLastCall().andReturn(method).times(2);
        executorFinder.getExecutor(method);
        EasyMock.expectLastCall().andReturn(executor);
        invocation.getThis();
        EasyMock.expectLastCall().andReturn(target).times(2);
        invocation.getArguments();
        EasyMock.expectLastCall().andReturn(arguments).times(2);
        executor.execute(target, method, arguments);
        EasyMock.expectLastCall().andReturn(Void.TYPE).times(2);
        EasyMock.replay(executorFinder, invocation, executor);

        Assert.assertEquals(Void.TYPE, repositoryInterceptor.invoke(invocation));
        // to test executors cache
        repositoryInterceptor.invoke(invocation);

        EasyMock.verify(executorFinder, invocation, executor);
    }
}
