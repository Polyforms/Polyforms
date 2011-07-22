package org.polyforms.di.spring.schema;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class PolyformsComponentScanBeanDefinitionParserTest {
    @Test
    public void createScanner() {
        final BeanDefinitionRegistry registry = EasyMock.createMock(BeanDefinitionRegistry.class);
        registry.containsBeanDefinition("abstractMethodOverrideProcessor");
        EasyMock.expectLastCall().andReturn(true);

        EasyMock.replay(registry);
        final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        final XmlReaderContext readerContext = new XmlReaderContext(null, null, null, null, reader, null);
        final ClassPathBeanDefinitionScanner scanner = new PolyformsComponentScanBeanDefinitionParser().createScanner(
                readerContext, true);
        Assert.assertTrue(scanner instanceof PolyformsClassPathBeanDefinitionScanner);
        EasyMock.verify(registry);
    }
}
