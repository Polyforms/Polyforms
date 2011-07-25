package org.polyforms.di.spring;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.di.BeanContainer;
import org.polyforms.di.spring.SpringBeanContainer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public final class SpringBeanContainerTest {
    private BeanFactory beanFactory;
    private BeanContainer beanContainer;

    @Before
    public void setUp() {
        beanFactory = EasyMock.createMock(BeanFactory.class);
        beanContainer = new SpringBeanContainer(beanFactory);
    }

    @Test
    public void containsBean() {
        beanFactory.getBean(Object.class);
        EasyMock.expectLastCall().andReturn(new Object());
        EasyMock.replay(beanFactory);

        Assert.assertTrue(beanContainer.containsBean(Object.class));
        EasyMock.verify(beanFactory);
    }

    @Test
    public void notContainsBean() {
        beanFactory.getBean(Object.class);
        EasyMock.expectLastCall().andThrow(new NoSuchBeanDefinitionException(Object.class));
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
