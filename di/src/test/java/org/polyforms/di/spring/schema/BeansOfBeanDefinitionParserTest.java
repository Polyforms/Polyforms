package org.polyforms.di.spring.schema;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.di.spring.BeansOfTypeFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.w3c.dom.Element;

public class BeansOfBeanDefinitionParserTest {
    private Element element;
    private BeansOfBeanDefinitionParser beanDefinitionParser;

    @Before
    public void setUp() {
        element = EasyMock.createMock(Element.class);
        beanDefinitionParser = new BeansOfBeanDefinitionParser();
    }

    @Test
    public void getBeanClass() {
        Assert.assertSame(BeansOfTypeFactoryBean.class, beanDefinitionParser.getBeanClass(element));
    }

    @Test
    public void parse() {
        final BeanDefinitionBuilder builder = EasyMock.createMock(BeanDefinitionBuilder.class);
        element.getAttribute("class");
        EasyMock.expectLastCall().andReturn("java.lang.Object");
        builder.addConstructorArgValue("java.lang.Object");
        EasyMock.expectLastCall().andReturn(builder);
        EasyMock.replay(element, builder);

        beanDefinitionParser.doParse(element, null, builder);
        EasyMock.verify(element, builder);
    }

    @Test(expected = BeanDefinitionValidationException.class)
    public void parseEmptyClass() {
        element.getAttribute("class");
        EasyMock.expectLastCall().andReturn("");
        EasyMock.replay(element);

        beanDefinitionParser.doParse(element, null, null);
        EasyMock.verify(element);
    }
}
