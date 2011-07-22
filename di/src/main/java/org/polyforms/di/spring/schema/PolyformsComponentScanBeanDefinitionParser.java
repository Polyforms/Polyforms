package org.polyforms.di.spring.schema;

import org.polyforms.di.spring.AbstractMethodOverrideProcessor;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;

/**
 * Parser for the &lt;polyforms:component-scan/&gt; element.
 * 
 * Extension of {@link ComponentScanBeanDefinitionParser} to support instantiation of interfaces and abstract classes.
 * It registers {@link AbstractMethodOverrideProcessor} if it does not exist in spring container .
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class PolyformsComponentScanBeanDefinitionParser extends ComponentScanBeanDefinitionParser {
    /**
     * {@inheritDoc}
     */
    @Override
    protected ClassPathBeanDefinitionScanner createScanner(final XmlReaderContext readerContext,
            final boolean useDefaultFilters) {
        return new PolyformsClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters);
    }
}
