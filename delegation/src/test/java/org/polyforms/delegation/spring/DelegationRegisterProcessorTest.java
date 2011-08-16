package org.polyforms.delegation.spring;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class DelegationRegisterProcessorTest {
    private DelegationRegisterProcessor delegationRegisterProcessor;
    private DelegationRegistry delegationRegistry;

    @Before
    public void setUp() {
        delegationRegisterProcessor = new DelegationRegisterProcessor();
        delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
    }

    @Test(expected = IllegalStateException.class)
    public void notConfigurableListableBeanFactory() {
        delegationRegisterProcessor
                .postProcessBeanDefinitionRegistry(EasyMock.createMock(BeanDefinitionRegistry.class));
    }

    @Test
    public void postProcessBeanDefinitionRegistry() {
        final DefaultListableBeanFactory beanFactory = EasyMock.createMock(DefaultListableBeanFactory.class);
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
        final BeanDefinition mockClass = EasyMock.createMock(BeanDefinition.class);
        final BeanDefinition mockInterface = EasyMock.createMock(BeanDefinition.class);

        beanFactory.getBean(DelegationRegistry.class);
        EasyMock.expectLastCall().andReturn(delegationRegistry);
        beanFactory.getBeansOfType(DelegationRegister.class);
        EasyMock.expectLastCall().andReturn(delegationRegisters);
        beanFactory.getBeanDefinitionNames();
        EasyMock.expectLastCall().andReturn(new String[] { "mockClass", "mockInterface" });
        beanFactory.getBeanDefinition("mockClass");
        EasyMock.expectLastCall().andReturn(mockClass);
        mockClass.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.getType("mockClass");
        EasyMock.expectLastCall().andReturn(MockClass.class);
        beanFactory.getBeanDefinition("mockInterface");
        EasyMock.expectLastCall().andReturn(mockInterface);
        mockInterface.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.getType("mockInterface");
        EasyMock.expectLastCall().andReturn(null);
        beanFactory
                .containsBeanDefinition("org.polyforms.delegation.spring.DelegationRegisterProcessorTest$MockInterface#0");
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.registerBeanDefinition(
                EasyMock.eq("org.polyforms.delegation.spring.DelegationRegisterProcessorTest$MockInterface#0"),
                EasyMock.isA(RootBeanDefinition.class));
        EasyMock.replay(beanFactory, mockClass, mockInterface, delegationRegistry);

        delegationRegisterProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        EasyMock.verify(beanFactory, mockClass, mockInterface, delegationRegistry);
    }

    @Test
    public void postProcessBeanFactory() {
        final ConfigurableListableBeanFactory beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        final BeanDefinition mockClass = EasyMock.createMock(BeanDefinition.class);
        final BeanDefinition mockInterface = EasyMock.createMock(BeanDefinition.class);

        beanFactory.getBean(DelegationRegistry.class);
        EasyMock.expectLastCall().andReturn(delegationRegistry);
        beanFactory.getBeanDefinitionNames();
        EasyMock.expectLastCall().andReturn(new String[] { "mockClass", "mockInterface" });
        beanFactory.getBeanDefinition("mockClass");
        EasyMock.expectLastCall().andReturn(mockClass);
        mockClass.isAbstract();
        EasyMock.expectLastCall().andReturn(false);
        beanFactory.getType("mockClass");
        EasyMock.expectLastCall().andReturn(MockClass.class);
        beanFactory.getBeanDefinition("mockInterface");
        EasyMock.expectLastCall().andReturn(mockInterface);
        mockInterface.isAbstract();
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(beanFactory, mockClass, mockInterface);

        delegationRegisterProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactory, mockClass, mockInterface);
    }

    public interface MockInterface {
    }

    public static class MockClass {
    }
}
