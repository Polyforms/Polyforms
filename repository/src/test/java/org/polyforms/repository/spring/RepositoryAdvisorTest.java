package org.polyforms.repository.spring;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.Proxy;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.aop.RepositoryInterceptor;
import org.polyforms.repository.spi.ExecutorFinder;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.springframework.aop.MethodMatcher;

public class RepositoryAdvisorTest {
    private RepositoryMatcher repositoryMatcher;
    private MethodMatcher methodMatcher;

    @Before
    public void setUp() {
        repositoryMatcher = EasyMock.createMock(RepositoryMatcher.class);
        final RepositoryInterceptor repositoryInterceptor = new RepositoryInterceptor(
                EasyMock.createMock(ExecutorFinder.class));
        methodMatcher = new RepositoryAdvisor(repositoryInterceptor, repositoryMatcher).getPointcut()
                .getMethodMatcher();
    }

    @Test
    public void matches() throws NoSuchMethodException {
        repositoryMatcher.matches(MockClass.class);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(repositoryMatcher);

        Assert.assertTrue(methodMatcher.matches(MockClass.class.getMethod("abstractMethod", new Class<?>[0]),
                MockClass.class));
        EasyMock.verify(repositoryMatcher);
    }

    @Test
    public void notMatches() throws NoSuchMethodException {
        repositoryMatcher.matches(MockClass.class);
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(repositoryMatcher);

        Assert.assertFalse(methodMatcher.matches(MockClass.class.getMethod("abstractMethod", new Class<?>[0]),
                MockClass.class));
        EasyMock.verify(repositoryMatcher);
    }

    @Test
    public void matchesEnhancer() throws NoSuchMethodException {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MockClass.class);
        enhancer.setCallbackTypes(new Class<?>[] { MethodInterceptor.class });
        final Class<?> proxyClass = enhancer.createClass();

        repositoryMatcher.matches(MockClass.class);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(repositoryMatcher);

        Assert.assertTrue(methodMatcher.matches(proxyClass.getMethod("abstractMethod", new Class<?>[0]), proxyClass));
        EasyMock.verify(repositoryMatcher);
    }

    @Test
    public void matchesProxy() throws NoSuchMethodException {
        final Class<?> proxyClass = Proxy.getProxyClass(MockInterface.class.getClassLoader(),
                new Class<?>[] { MockInterface.class });

        repositoryMatcher.matches(MockInterface.class);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(repositoryMatcher);

        Assert.assertTrue(methodMatcher.matches(
                proxyClass.getMethod("abstractMethod", new Class<?>[] { Object.class }), proxyClass));
        EasyMock.verify(repositoryMatcher);
    }

    @Test
    public void notMatchesConcret() throws NoSuchMethodException {
        Assert.assertFalse(methodMatcher.matches(MockClass.class.getMethod("concretMethod", new Class<?>[0]),
                MockClass.class));
    }

    public static abstract class MockClass {
        public void concretMethod() {
        }

        public abstract void abstractMethod();
    }

    public interface MockInterface extends GenericInterface<Integer> {
    }

    public interface GenericInterface<T> {
        void abstractMethod(T object);
    }
}
