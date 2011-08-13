package org.polyforms.delegation.converter;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.config.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NameTransformers;
import org.modelmapper.convention.NamingConventions;
import org.modelmapper.spi.ConditionalConverter;
import org.springframework.test.util.ReflectionTestUtils;

public class ModelMapperFactoryBeanTest {
    private ModelMapper modelMapper;
    private Configuration configuration;
    private ModelMapperFactoryBean factory;

    @Before
    public void setUp() {
        modelMapper = EasyMock.createMock(ModelMapper.class);
        configuration = EasyMock.createMock(Configuration.class);
        modelMapper.getConfiguration();
        EasyMock.expectLastCall().andReturn(configuration);

        factory = new ModelMapperFactoryBean();
        ReflectionTestUtils.setField(factory, "modelMapper", modelMapper);
    }

    @Test
    public void getObject() {
        Assert.assertSame(modelMapper, factory.getObject());
    }

    @Test
    public void isSingleton() {
        Assert.assertTrue(factory.isSingleton());
    }

    @Test
    public void getObjectType() {
        Assert.assertSame(ModelMapper.class, factory.getObjectType());
    }

    @Test
    public void setFieldMatching() {
        configuration.enableFieldMatching(true);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setFieldMatching(true);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setIgnoreAmbiguity() {
        configuration.ignoreAmbiguity(true);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setIgnoreAmbiguity(true);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setFieldAccessLevel() {
        configuration.setFieldAccessLevel(AccessLevel.PUBLIC);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setFieldAccessLevel(AccessLevel.PUBLIC);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setMethodAccessLevel() {
        configuration.setMethodAccessLevel(AccessLevel.PUBLIC);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setMethodAccessLevel(AccessLevel.PUBLIC);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setMatchingStrategy() {
        configuration.setMatchingStrategy(MatchingStrategies.LOOSE);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setMatchingStrategy(MatchingStrategies.LOOSE);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setSourceNamingConvention() {
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setDestinationNamingConvention() {
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setDestinationNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setSourceNameTokenizer() {
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setDestinationNameTokenizer() {
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setSourceNameTransformer() {
        configuration.setSourceNameTransformer(NameTransformers.JAVABEANS_ACCESSOR);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setSourceNameTransformer(NameTransformers.JAVABEANS_ACCESSOR);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setDestinationNameTransformer() {
        configuration.setDestinationNameTransformer(NameTransformers.JAVABEANS_ACCESSOR);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setDestinationNameTransformer(NameTransformers.JAVABEANS_ACCESSOR);
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setConverters() {
        final List<ConditionalConverter<?, ?>> defaultConverters = new ArrayList<ConditionalConverter<?, ?>>();
        configuration.getConverters();
        EasyMock.expectLastCall().andReturn(defaultConverters);
        EasyMock.replay(modelMapper, configuration);

        final ConditionalConverter<?, ?> converter = EasyMock.createMock(ConditionalConverter.class);
        final List<ConditionalConverter<?, ?>> converters = new ArrayList<ConditionalConverter<?, ?>>();
        converters.add(converter);
        factory.setConverters(converters);
        Assert.assertTrue(defaultConverters.contains(converter));
        EasyMock.verify(modelMapper, configuration);
    }

    @Test
    public void setProvider() {
        final Provider<?> provider = EasyMock.createMock(Provider.class);
        configuration.setProvider(provider);
        EasyMock.expectLastCall().andReturn(configuration);
        EasyMock.replay(modelMapper, configuration);

        factory.setProvider(provider);
        EasyMock.verify(modelMapper, configuration);
    }
}
