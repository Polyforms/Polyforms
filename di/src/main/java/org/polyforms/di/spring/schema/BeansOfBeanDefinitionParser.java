package org.polyforms.di.spring.schema;

import org.polyforms.di.spring.BeansOfTypeFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Parser for the &lt;polyforms:beansOf/&gt; element.
 * 
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class BeansOfBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(final Element element) {
        return BeansOfTypeFactoryBean.class;
    }

    @Override
    protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder) {
        final String beanClass = element.getAttribute("class");
        if (!StringUtils.hasText(beanClass)) {
            throw new BeanDefinitionValidationException("'class' must be set in 'beansOf'");
        }
        builder.addConstructorArgValue(beanClass);
    }
}
