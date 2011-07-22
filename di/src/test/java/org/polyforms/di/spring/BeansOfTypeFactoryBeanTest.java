package org.polyforms.di.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;

public class BeansOfTypeFactoryBeanTest {
    private BeansOfTypeFactoryBean<Object> factoryBean;

    @Before
    public void setUp() {
        factoryBean = new BeansOfTypeFactoryBean<Object>(Object.class);
    }

    @Test
    public void getObject() {
        Map<String, Object> beanMap = new HashMap<String, Object>();
        Object bean = new Object();
        beanMap.put("bean", bean);

        ListableBeanFactory beanFactory = EasyMock.createMock(ListableBeanFactory.class);
        factoryBean.setBeanFactory(beanFactory);
        beanFactory.getBeansOfType(Object.class);
        EasyMock.expectLastCall().andReturn(beanMap);
        EasyMock.replay(beanFactory);

        Collection<Object> beans = factoryBean.getObject();
        Assert.assertEquals(1, beans.size());
        Assert.assertSame(bean, beans.iterator().next());
        // Just for testing cache
        factoryBean.getObject();
        EasyMock.verify(beanFactory);
    }

    @Test
    public void getObjectType() {
        Assert.assertSame(Collection.class, factoryBean.getObjectType());
    }

    @Test
    public void isSingleton() {
        Assert.assertTrue(factoryBean.isSingleton());
    }

    @Test
    public void getObjectForNull() throws Exception {
        FactoryBean<Collection<Object>> factoryBean = new BeansOfTypeFactoryBean<Object>(null);
        Assert.assertTrue(((Collection<?>) factoryBean.getObject()).isEmpty());
    }
}
