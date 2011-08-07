package org.polyforms.delegation.spring;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.AnnotatedDelegationRegister;
import org.polyforms.di.spring.util.BeanFactoryVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class DelegationRegisterProcessorTest {
    @Test
    public void postProcessBeanFactory() {
        final DelegationRegisterProcessor delegationRegisterProcessor = new DelegationRegisterProcessor();
        final BeanFactoryVisitor beanFactoryVisitor = EasyMock.createMock(BeanFactoryVisitor.class);
        ReflectionTestUtils.setField(delegationRegisterProcessor, "beanFactoryVisitor", beanFactoryVisitor);

        final ConfigurableListableBeanFactory beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        final DelegationRegistry delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
        beanFactory.getBean(DelegationRegistry.class);
        EasyMock.expectLastCall().andReturn(delegationRegistry);
        final DelegationRegister<MockInterface> delegationRegister = new DelegationRegister<MockInterface>() {
            public void register(final MockInterface source) {
            }
        };
        final Map<String, Object> delegationRegisters = new HashMap<String, Object>();
        delegationRegisters.put("delegationRegister", delegationRegister);
        beanFactory.getBeansOfType(DelegationRegister.class);
        EasyMock.expectLastCall().andReturn(delegationRegisters);
        beanFactoryVisitor.visit(EasyMock.same(beanFactory), EasyMock.isA(AnnotatedDelegationRegister.class));
        EasyMock.replay(beanFactoryVisitor, beanFactory);

        delegationRegisterProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactoryVisitor, beanFactory);
    }

    public interface MockInterface {
    }
}
