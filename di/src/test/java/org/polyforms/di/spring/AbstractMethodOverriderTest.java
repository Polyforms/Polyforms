package org.polyforms.di.spring;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.di.spring.AbstractMethodOverrider.UnsupportedMethodReplacer;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.MethodOverride;
import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.beans.factory.support.ReplaceOverride;

public class AbstractMethodOverriderTest {
    private AbstractBeanDefinition beanDefinition;
    private MethodOverrides methodOverrides;
    private ConfigurableListableBeanFactory beanFactory;
    private BeanClassVisitor beanClassVisitor;

    @Before
    public void setUp() {
        beanDefinition = EasyMock.createMock(AbstractBeanDefinition.class);
        methodOverrides = EasyMock.createMock(MethodOverrides.class);
        beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        beanClassVisitor = new AbstractMethodOverrider(beanFactory);
    }

    @Test
    public void skipConcretClass() {
        beanClassVisitor.visit(null, null, Object.class);
    }

    @Test
    public void visit() throws NoSuchMethodException {
        beanFactory.containsBean("__UNSUPPORTED_METHOD_REPLACER");
        EasyMock.expectLastCall().andReturn(true);
        beanDefinition.getMethodOverrides();
        EasyMock.expectLastCall().andReturn(methodOverrides);
        methodOverrides.getOverride(MockClass.class.getMethod("abstractMethod", new Class<?>[0]));
        EasyMock.expectLastCall().andReturn(null);
        methodOverrides.addOverride(EasyMock.isA(ReplaceOverride.class));
        EasyMock.replay(beanFactory, beanDefinition, methodOverrides);

        beanClassVisitor.visit(null, beanDefinition, MockClass.class);
        EasyMock.verify(beanFactory, beanDefinition, methodOverrides);
    }

    @Test
    public void skipOverridedMethod() throws NoSuchMethodException {
        beanFactory.containsBean("__UNSUPPORTED_METHOD_REPLACER");
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.registerSingleton(EasyMock.eq("__UNSUPPORTED_METHOD_REPLACER"),
                EasyMock.isA(UnsupportedMethodReplacer.class));
        beanDefinition.getMethodOverrides();
        EasyMock.expectLastCall().andReturn(methodOverrides);
        methodOverrides.getOverride(MockInterface.class.getMethod("abstractMethod", new Class<?>[0]));
        EasyMock.expectLastCall().andReturn(EasyMock.createMock(MethodOverride.class));
        EasyMock.replay(beanFactory, beanDefinition, methodOverrides);

        beanClassVisitor.visit(null, beanDefinition, MockInterface.class);
        EasyMock.verify(beanFactory, beanDefinition, methodOverrides);
    }

    public static abstract class MockClass {
        public abstract void abstractMethod();

        public void concretMethod() {
        }
    }

    public interface MockInterface {
        void abstractMethod();
    }
}
