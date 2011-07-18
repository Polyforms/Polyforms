package org.polyforms.di.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.polyforms.di.container.BeanContainer;
import org.polyforms.di.container.BeanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("ComponentScannerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public final class SpringBeanContainerTest {
    @Autowired
    private BeanContainer beanContainer;

    @Test
    public void containsBean() {
        Assert.assertTrue(beanContainer.containsBean(BeanContainer.class));
    }

    @Test
    public void notContainsBean() {
        Assert.assertFalse(beanContainer.containsBean(SpringBeanContainerTest.class));
    }

    @Test
    public void getBeanByType() {
        Assert.assertSame(beanContainer, beanContainer.getBean(BeanContainer.class));
    }

    @Test(expected = BeanNotFoundException.class)
    public void getInexistentBean() {
        beanContainer.getBean(SpringBeanContainerTest.class);
    }

    @Test
    public void getBeanByNameAndType() {
        Assert.assertSame(beanContainer, beanContainer.getBean("springBeanContainer", BeanContainer.class));
    }

    @Test(expected = BeanNotFoundException.class)
    public void getBeanOfIncorrectType() {
        beanContainer.getBean("springBeanContainer", SpringBeanContainerTest.class);
    }

    @Test
    public void getBeansByType() {
        Assert.assertSame(beanContainer, beanContainer.getBeans(BeanContainer.class).toArray()[0]);
    }
}
