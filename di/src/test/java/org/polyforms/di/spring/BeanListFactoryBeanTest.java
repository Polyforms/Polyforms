package org.polyforms.di.spring;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class BeanListFactoryBeanTest {
    @Autowired
    private FactoryBean<Collection<GenericConverter>> factoryBean;

    @Test
    public void getObjectType() {
        Assert.assertSame(Collection.class, factoryBean.getObjectType());
    }

    @Test
    public void isSingleton() {
        Assert.assertTrue(factoryBean.isSingleton());
    }

    @Test
    public void objectCached() throws Exception {
        Assert.assertSame(factoryBean.getObject(), factoryBean.getObject());
    }
}
