package org.polyforms.di.spring;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.di.spring.AbstractMethodOverrideProcessor.UnsupportedMethodReplacer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.MethodOverride;
import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.beans.factory.support.ReplaceOverride;

public class AbstractMethodOverrideProcessorTest {
    private AbstractBeanDefinition beanDefinitionA;
    private AbstractBeanDefinition beanDefinitionB;
    private MethodOverrides methodOverrides;
    private ConfigurableListableBeanFactory beanFactory;
    private AbstractMethodOverrideProcessor abstractMethodOverrideProcessor;

    @Before
    public void setUp() {
        beanDefinitionA = EasyMock.createMock(AbstractBeanDefinition.class);
        beanDefinitionB = EasyMock.createMock(AbstractBeanDefinition.class);
        methodOverrides = EasyMock.createMock(MethodOverrides.class);
        beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        abstractMethodOverrideProcessor = new AbstractMethodOverrideProcessor();
    }

    @Test
    public void visit() throws NoSuchMethodException, ClassNotFoundException {
        beanFactory.getBeanDefinitionNames();
        EasyMock.expectLastCall().andReturn(new String[] { "mockClass", "mockinterface" });
        beanFactory.getBeanDefinition("mockClass");
        EasyMock.expectLastCall().andReturn(beanDefinitionA);
        beanDefinitionA.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinitionA.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(MockClass.class);
        beanFactory.getBeanDefinition("mockinterface");
        EasyMock.expectLastCall().andReturn(beanDefinitionB);
        beanDefinitionB.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinitionB.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(null);
        beanDefinitionB.getParentName();
        EasyMock.expectLastCall().andReturn(null);
        beanFactory.containsBean("__UNSUPPORTED_METHOD_REPLACER");
        EasyMock.expectLastCall().andReturn(true);
        beanDefinitionA.getMethodOverrides();
        EasyMock.expectLastCall().andReturn(methodOverrides);
        methodOverrides.getOverride(MockClass.class.getMethod("abstractMethod", new Class<?>[0]));
        EasyMock.expectLastCall().andReturn(null);
        methodOverrides.addOverride(EasyMock.isA(ReplaceOverride.class));
        EasyMock.replay(beanFactory, beanDefinitionA, beanDefinitionB, methodOverrides);

        abstractMethodOverrideProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactory, beanDefinitionA, beanDefinitionB, methodOverrides);
    }

    @Test
    public void skipInexistedClass() throws NoSuchMethodException, ClassNotFoundException {
        beanFactory.getBeanDefinitionNames();
        EasyMock.expectLastCall().andReturn(new String[] { "mockinterface" });
        beanFactory.getBeanDefinition("mockinterface");
        EasyMock.expectLastCall().andReturn(beanDefinitionB);
        beanDefinitionB.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinitionB.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andThrow(new ClassNotFoundException());
        EasyMock.replay(beanFactory, beanDefinitionB);

        abstractMethodOverrideProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactory, beanDefinitionB);
    }

    @Test
    public void skipOverridedMethod() throws NoSuchMethodException, ClassNotFoundException {
        final AbstractBeanDefinition parentBeanDefinition = EasyMock.createMock(AbstractBeanDefinition.class);

        beanFactory.getBeanDefinitionNames();
        EasyMock.expectLastCall().andReturn(new String[] { "mockClass", "mockinterface" });
        beanFactory.getBeanDefinition("mockClass");
        EasyMock.expectLastCall().andReturn(beanDefinitionA);
        beanDefinitionA.isAbstract();
        EasyMock.expectLastCall().andReturn(true);

        beanFactory.getBeanDefinition("mockinterface");
        EasyMock.expectLastCall().andReturn(beanDefinitionB);
        beanDefinitionB.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinitionB.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(null);

        beanDefinitionB.getParentName();
        EasyMock.expectLastCall().andReturn("parent");
        beanFactory.getBeanDefinition("parent");
        EasyMock.expectLastCall().andReturn(parentBeanDefinition);
        parentBeanDefinition.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(MockInterface.class);
        beanFactory.containsBean("__UNSUPPORTED_METHOD_REPLACER");
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.registerSingleton(EasyMock.eq("__UNSUPPORTED_METHOD_REPLACER"),
                EasyMock.isA(UnsupportedMethodReplacer.class));
        beanDefinitionB.getMethodOverrides();
        EasyMock.expectLastCall().andReturn(methodOverrides);
        methodOverrides.getOverride(MockInterface.class.getMethod("abstractMethod", new Class<?>[0]));
        EasyMock.expectLastCall().andReturn(EasyMock.createMock(MethodOverride.class));
        EasyMock.replay(beanFactory, beanDefinitionA, beanDefinitionB, parentBeanDefinition, methodOverrides);

        abstractMethodOverrideProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactory, beanDefinitionA, beanDefinitionB, parentBeanDefinition, methodOverrides);
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
