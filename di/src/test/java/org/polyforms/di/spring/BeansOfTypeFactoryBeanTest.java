package org.polyforms.di.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.ListableBeanFactory;

public class BeansOfTypeFactoryBeanTest {
    private BeansOfTypeFactoryBean<Object> factoryBean;

    @Before
    public void setUp() {
        factoryBean = new BeansOfTypeFactoryBean<Object>(Object.class);
    }

    @Test
    public void getObject() throws Exception {
        final Map<String, Object> beanMap = new HashMap<String, Object>();
        final Object bean = new Object();
        beanMap.put("bean", bean);

        final ListableBeanFactory beanFactory = EasyMock.createMock(ListableBeanFactory.class);
        beanFactory.getBeansOfType(Object.class);
        EasyMock.expectLastCall().andReturn(beanMap);
        EasyMock.replay(beanFactory);

        factoryBean.setBeanFactory(beanFactory);
        factoryBean.afterPropertiesSet();

        final Collection<Object> beans = factoryBean.getObject();
        Assert.assertEquals(1, beans.size());
        Assert.assertSame(bean, beans.iterator().next());
        EasyMock.verify(beanFactory);
    }

    @Test
    public void getObjectType() {
        Assert.assertSame(Collection.class, factoryBean.getObjectType());
    }

    @Test
    public void getObjectForNull() throws Exception {
        final BeansOfTypeFactoryBean<Object> factoryBean = new BeansOfTypeFactoryBean<Object>(null);
        factoryBean.afterPropertiesSet();
        Assert.assertTrue(((Collection<?>) factoryBean.getObject()).isEmpty());
    }
}
