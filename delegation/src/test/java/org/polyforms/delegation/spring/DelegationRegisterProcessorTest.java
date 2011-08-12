package org.polyforms.delegation.spring;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.AnnotatedDelegationRegister;
import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.test.util.ReflectionTestUtils;

public class DelegationRegisterProcessorTest {
    private DelegationRegisterProcessor delegationRegisterProcessor;
    private DelegationRegistry delegationRegistry;

    @Before
    public void setUp() {
        delegationRegisterProcessor = new DelegationRegisterProcessor();
        delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
    }

    @Test
    public <T> void postProcessBeanDefinitionRegistry() {
        final DefaultListableBeanFactory beanFactory = EasyMock.createMock(DefaultListableBeanFactory.class);
        final BeanFactoryVisitor beanFactoryVisitor = new BeanFactoryVisitor() {
            public void visit(ConfigurableListableBeanFactory beanFactory, BeanClassVisitor visitor) {
                visitor.visit(null, null, MockClass.class);
            }
        };
        ReflectionTestUtils.setField(delegationRegisterProcessor, "beanFactoryVisitor", beanFactoryVisitor);

        beanFactory.getBean(DelegationRegistry.class);
        EasyMock.expectLastCall().andReturn(delegationRegistry);
        final DelegationRegister<MockInterface> delegationRegisterA = new DelegationRegister<MockInterface>() {
            public void register(final MockInterface source) {
            }
        };
        final DelegationRegister<MockClass> delegationRegisterB = new DelegationRegister<MockClass>() {
            public void register(final MockClass source) {
            }
        };
        final Map<String, Object> delegationRegisters = new HashMap<String, Object>();
        delegationRegisters.put("delegationRegisterA", delegationRegisterA);
        delegationRegisters.put("delegationRegisterB", delegationRegisterB);
        beanFactory.getBeansOfType(DelegationRegister.class);
        EasyMock.expectLastCall().andReturn(delegationRegisters);
        beanFactory
                .containsBeanDefinition("org.polyforms.delegation.spring.DelegationRegisterProcessorTest$MockInterface#0");
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.registerBeanDefinition(
                EasyMock.eq("org.polyforms.delegation.spring.DelegationRegisterProcessorTest$MockInterface#0"),
                EasyMock.isA(RootBeanDefinition.class));
        EasyMock.replay(beanFactory, delegationRegistry);

        delegationRegisterProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        EasyMock.verify(beanFactory, delegationRegistry);
    }

    @Test
    public void postProcessBeanFactory() {
        final ConfigurableListableBeanFactory beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        final BeanFactoryVisitor beanFactoryVisitor = EasyMock.createMock(BeanFactoryVisitor.class);
        ReflectionTestUtils.setField(delegationRegisterProcessor, "beanFactoryVisitor", beanFactoryVisitor);

        beanFactory.getBean(DelegationRegistry.class);
        EasyMock.expectLastCall().andReturn(delegationRegistry);
        beanFactoryVisitor.visit(EasyMock.same(beanFactory), EasyMock.isA(AnnotatedDelegationRegister.class));
        EasyMock.replay(beanFactoryVisitor, beanFactory);

        delegationRegisterProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactoryVisitor, beanFactory);
    }

    public interface MockInterface {
    }

    public static class MockClass {
    }
}
