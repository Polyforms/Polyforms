package org.polyforms.di.spring;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class AbstractMethodOverrideProcessorTest {
    @Test
    public void postProcessBeanFactory() {
        final BeanFactoryVisitor beanFactoryVisitor = EasyMock.createMock(BeanFactoryVisitor.class);
        final ConfigurableListableBeanFactory beanFactory = EasyMock.createMock(ConfigurableListableBeanFactory.class);
        beanFactoryVisitor.visit(EasyMock.same(beanFactory), EasyMock.isA(AbstractMethodOverrider.class));
        EasyMock.replay(beanFactoryVisitor);

        final AbstractMethodOverrideProcessor abstractMethodOverrideProcessor = new AbstractMethodOverrideProcessor();
        ReflectionTestUtils.setField(abstractMethodOverrideProcessor, "beanFactoryVisitor", beanFactoryVisitor);
        abstractMethodOverrideProcessor.postProcessBeanFactory(beanFactory);
        EasyMock.verify(beanFactoryVisitor);
    }
}
