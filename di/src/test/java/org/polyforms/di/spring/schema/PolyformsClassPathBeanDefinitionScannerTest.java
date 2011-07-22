package org.polyforms.di.spring.schema;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

public class PolyformsClassPathBeanDefinitionScannerTest {
    private BeanDefinitionRegistry registry;

    @Before
    public void setUp() {
        registry = EasyMock.createMock(BeanDefinitionRegistry.class);
    }

    @Test
    public void isCandidateComponent() {
        registry.containsBeanDefinition("abstractMethodOverrideProcessor");
        EasyMock.expectLastCall().andReturn(false);
        registry.registerBeanDefinition(EasyMock.eq("abstractMethodOverrideProcessor"),
                EasyMock.isA(RootBeanDefinition.class));

        runTest();
    }

    @Test
    public void createScannerWithProcessor() {
        registry.containsBeanDefinition("abstractMethodOverrideProcessor");
        EasyMock.expectLastCall().andReturn(true);

        runTest();
    }

    private void runTest() {
        final AnnotationMetadata metaData = EasyMock.createMock(AnnotationMetadata.class);
        final AnnotatedBeanDefinition beanDefinition = EasyMock.createMock(AnnotatedBeanDefinition.class);
        beanDefinition.getMetadata();
        EasyMock.expectLastCall().andReturn(metaData);
        metaData.isIndependent();
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(registry, beanDefinition, metaData);

        final PolyformsClassPathBeanDefinitionScanner scanner = new PolyformsClassPathBeanDefinitionScanner(registry,
                true);
        Assert.assertTrue(scanner.isCandidateComponent(beanDefinition));
        EasyMock.verify(registry, beanDefinition, metaData);
    }
}
