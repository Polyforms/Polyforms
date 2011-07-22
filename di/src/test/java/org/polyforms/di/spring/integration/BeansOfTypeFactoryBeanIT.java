package org.polyforms.di.spring.integration;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class BeansOfTypeFactoryBeanIT {
    @Autowired
    private BeanFactory beanFactory;

    @Test
    @SuppressWarnings("unchecked")
    public void getBeansOf() {
        final Collection<BeanFactoryPostProcessor> processors = (Collection<BeanFactoryPostProcessor>) beanFactory
                .getBean("processors");
        Assert.assertFalse(processors.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getNoBeansOf() {
        final Collection<GenericConverter> converters = (Collection<GenericConverter>) beanFactory
                .getBean("converters");
        Assert.assertTrue(converters.isEmpty());
    }
}
