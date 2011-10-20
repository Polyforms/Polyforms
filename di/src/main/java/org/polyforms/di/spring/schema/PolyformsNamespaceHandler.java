package org.polyforms.di.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler} for the namespace <code>polyforms</code>.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class PolyformsNamespaceHandler extends NamespaceHandlerSupport {
    /**
     * {@inheritDoc}
     */
    public void init() {
        registerBeanDefinitionParser("component-scan", new PolyformsComponentScanBeanDefinitionParser());
        registerBeanDefinitionParser("beansOf", new BeansOfBeanDefinitionParser());
    }
}
