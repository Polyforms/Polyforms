package org.polyforms.di.spring.util;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;
import org.polyforms.di.spring.util.support.GenericBeanFactoryVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

public class GenericBeanFactoryVisitorTest {
    private ConfigurableListableBeanFactory beanFactory;
    private BeanClassVisitor beanClassVisitor;
    private AbstractBeanDefinition beanDefinition;
    private AbstractBeanDefinition parentBeanDefinition;
    private BeanFactoryVisitor beanFactoryVisitor;

    @Before
    public void setUp() {
        beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        beanClassVisitor = EasyMock.createMock(BeanClassVisitor.class);
        beanDefinition = EasyMock.createMock(AbstractBeanDefinition.class);
        parentBeanDefinition = EasyMock.createMock(AbstractBeanDefinition.class);
        beanFactoryVisitor = new GenericBeanFactoryVisitor();
    }

    @Test
    public void skipAbstractBean() {
        prepareBeanDefinition();

        beanDefinition.isAbstract();
        EasyMock.expectLastCall().andReturn(true);

        runTest();
    }

    @Test
    public void visitBean() throws ClassNotFoundException {
        prepareBeanDefinition();

        beanDefinition.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinition.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(Object.class);
        beanClassVisitor.visit("bean", beanDefinition, Object.class);
        EasyMock.replay(beanClassVisitor);

        runTest();
        EasyMock.verify(beanClassVisitor);
    }

    @Test
    public void visitBeanWithNotExistClass() throws ClassNotFoundException {
        prepareBeanDefinition();

        beanDefinition.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinition.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andThrow(new ClassNotFoundException());

        runTest();
    }

    @Test
    public void visitBeanWithParent() throws ClassNotFoundException {
        prepareBeanDefinition();

        beanDefinition.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinition.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(null);
        beanDefinition.getParentName();
        EasyMock.expectLastCall().andReturn("parent");
        beanFactory.getBeanDefinition("parent");
        EasyMock.expectLastCall().andReturn(parentBeanDefinition);
        parentBeanDefinition.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(Object.class);
        beanClassVisitor.visit("bean", beanDefinition, Object.class);
        EasyMock.replay(parentBeanDefinition, beanClassVisitor);

        runTest();
        EasyMock.verify(parentBeanDefinition, beanClassVisitor);
    }

    @Test
    public void skipBeanWithoutClass() throws ClassNotFoundException {
        prepareBeanDefinition();

        beanDefinition.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanDefinition.resolveBeanClass(EasyMock.isA(ClassLoader.class));
        EasyMock.expectLastCall().andReturn(null);
        beanDefinition.getParentName();
        EasyMock.expectLastCall().andReturn(null);

        runTest();
    }

    private void prepareBeanDefinition() {
        beanFactory.getBeanDefinitionNames();
        EasyMock.expectLastCall().andReturn(new String[] { "bean" });
        beanFactory.getBeanDefinition("bean");
        EasyMock.expectLastCall().andReturn(beanDefinition);
    }

    private void runTest() {
        EasyMock.replay(beanFactory, beanDefinition);

        beanFactoryVisitor.visit(beanFactory, beanClassVisitor);
        EasyMock.verify(beanFactory, beanDefinition);
    }
}
