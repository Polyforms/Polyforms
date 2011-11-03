package org.polyforms.delegation.spring;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.BeanContainer;
import org.springframework.beans.factory.ListableBeanFactory;

public final class SpringBeanContainerTest {
    private ListableBeanFactory beanFactory;
    private BeanContainer beanContainer;

    @Before
    public void setUp() {
        beanFactory = EasyMock.createMock(ListableBeanFactory.class);
        beanContainer = new SpringBeanContainer(beanFactory);
    }

    @Test
    public void containsBean() {
        beanFactory.getBeanNamesForType(Object.class);
        EasyMock.expectLastCall().andReturn(new String[] { "bean" });
        EasyMock.replay(beanFactory);

        Assert.assertTrue(beanContainer.containsBean(Object.class));
        EasyMock.verify(beanFactory);
    }

    @Test
    public void notContainsBean() {
        beanFactory.getBeanNamesForType(Object.class);
        EasyMock.expectLastCall().andReturn(new String[0]);
        EasyMock.replay(beanFactory);

        Assert.assertFalse(beanContainer.containsBean(Object.class));
        EasyMock.verify(beanFactory);
    }

    @Test
    public void getBeanByType() {
        final Object bean = new Object();

        beanFactory.getBean(Object.class);
        EasyMock.expectLastCall().andReturn(bean);
        EasyMock.replay(beanFactory);

        Assert.assertSame(bean, beanContainer.getBean(Object.class));
        EasyMock.verify(beanFactory);
    }

    @Test
    public void getBeanByNameAndType() {
        final Object bean = new Object();

        beanFactory.getBean("bean", Object.class);
        EasyMock.expectLastCall().andReturn(bean);
        EasyMock.replay(beanFactory);

        Assert.assertSame(bean, beanContainer.getBean("bean", Object.class));
        EasyMock.verify(beanFactory);
    }
}
