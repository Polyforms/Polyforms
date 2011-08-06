package org.polyforms.delegation.builder.support;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.support.Cglib2ProxyFactory.MethodVisitor;

public class Cglib2ProxyFactoryTest {
    private MethodVisitor methodVisitor;
    private ProxyFactory proxyFactory;

    @Before
    public void setUp() {
        methodVisitor = EasyMock.createMock(MethodVisitor.class);
        proxyFactory = new Cglib2ProxyFactory(methodVisitor);
    }

    @Test
    public void getProxyForClass() throws NoSuchMethodException {
        methodVisitor.visit(MockClass.class.getMethod("mockMethod", new Class<?>[0]));
        EasyMock.replay(methodVisitor);

        final MockClass mockClass = proxyFactory.getProxy(MockClass.class);
        mockClass.mockMethod();
        EasyMock.verify(methodVisitor);
    }

    @Test
    public void getProxyForInterface() throws NoSuchMethodException {
        methodVisitor.visit(MockInterface.class.getMethod("mockMethod", new Class<?>[0]));
        EasyMock.replay(methodVisitor);

        final MockInterface mockInterface = proxyFactory.getProxy(MockInterface.class);
        mockInterface.mockMethod();
        EasyMock.verify(methodVisitor);
    }

    @Test
    public void getProxyForNull() {
        Assert.assertNull(proxyFactory.getProxy(null));
    }

    @Test
    public void getProxyForFinalClass() {
        Assert.assertNull(proxyFactory.getProxy(String.class));
    }

    public static class MockClass {
        protected MockClass() {
        }

        public MockClass(final String string) {
        }

        public void mockMethod() {
        }
    }

    public interface MockInterface {
        void mockMethod();
    }
}
