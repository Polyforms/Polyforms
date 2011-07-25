package org.polyforms.delegation.spring.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.internal.InheritingConfiguration;
import org.modelmapper.internal.converter.ConverterStore;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MatchingStrategy;
import org.modelmapper.spi.NameTokenizer;
import org.modelmapper.spi.NameTransformer;
import org.modelmapper.spi.NamingConvention;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ReflectionUtils;

/**
 * A factory for a {@link ModelMapper}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class ModelMapperFactoryBean implements FactoryBean<ModelMapper> {
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * {@inheritDoc}
     */
    public ModelMapper getObject() {
        return modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getObjectType() {
        return ModelMapper.class;
    }

    /**
     * @see org.modelmapper.config.Configuration#enableFieldMatching(boolean)
     */
    public void setFieldMatching(final boolean fieldMatching) {
        modelMapper.getConfiguration().enableFieldMatching(fieldMatching);
    }

    /**
     * @see org.modelmapper.config.Configuration#setFieldAccessLevel(AccessLevel)
     */
    public void setFieldAccessLevel(final AccessLevel accessLevel) {
        modelMapper.getConfiguration().setFieldAccessLevel(accessLevel);
    }

    /**
     * @see org.modelmapper.config.Configuration#setMethodAccessLevel(AccessLevel)
     */
    public void setMethodAccessLevel(final AccessLevel accessLevel) {
        modelMapper.getConfiguration().setMethodAccessLevel(accessLevel);
    }

    /**
     * @see org.modelmapper.config.Configuration#setMatchingStrategy(MatchingStrategy)
     */
    public void setMatchingStrategy(final MatchingStrategy matchingStrategy) {
        modelMapper.getConfiguration().setMatchingStrategy(matchingStrategy);
    }

    /**
     * @see org.modelmapper.config.Configuration#setSourceNamingConvention(NamingConvention)
     */
    public void setSourceNamingConvention(final NamingConvention namingConvention) {
        modelMapper.getConfiguration().setSourceNamingConvention(namingConvention);
    }

    /**
     * @see org.modelmapper.config.Configuration#setDestinationNamingConvention(NamingConvention)
     */
    public void setDestinationNamingConvention(final NamingConvention namingConvention) {
        modelMapper.getConfiguration().setDestinationNamingConvention(namingConvention);
    }

    /**
     * @see org.modelmapper.config.Configuration#setSourceNameTokenizer(NameTokenizer)
     */
    public void setSourceNameTokenizer(final NameTokenizer nameTokenizer) {
        modelMapper.getConfiguration().setSourceNameTokenizer(nameTokenizer);
    }

    /**
     * @see org.modelmapper.config.Configuration#setDestinationNameTokenizer(NameTokenizer)
     */
    public void setDestinationNameTokenizer(final NameTokenizer nameTokenizer) {
        modelMapper.getConfiguration().setDestinationNameTokenizer(nameTokenizer);
    }

    /**
     * @see org.modelmapper.config.Configuration#setSourceNameTransformer(NameTransformer)
     */
    public void setSourceNameTransformer(final NameTransformer nameTransformer) {
        modelMapper.getConfiguration().setSourceNameTransformer(nameTransformer);
    }

    /**
     * @see org.modelmapper.config.Configuration#setDestinationNameTransformer(NameTransformer)
     */
    public void setDestinationNameTransformer(final NameTransformer nameTransformer) {
        modelMapper.getConfiguration().setDestinationNameTransformer(nameTransformer);
    }

    /**
     * @see org.modelmapper.config.Configuration#addConverter(ConditionalConverter)
     */
    @SuppressWarnings("unchecked")
    public void setConverters(final List<ConditionalConverter<?, ?>> converters) {
        final ConverterStore converterStore = ((InheritingConfiguration) modelMapper.getConfiguration()).converterStore;
        final Field field = ReflectionUtils.findField(ConverterStore.class, "converters");
        ReflectionUtils.makeAccessible(field);

        final List<ConditionalConverter<?, ?>> buildinConverters = new ArrayList<ConditionalConverter<?, ?>>(
                (List<ConditionalConverter<?, ?>>) ReflectionUtils.getField(field, converterStore));
        for (final ConditionalConverter<?, ?> converter : converters) {
            buildinConverters.add(converter);
        }
        ReflectionUtils.setField(field, converterStore, buildinConverters);
    }

    /**
     * @see org.modelmapper.config.Configuration#setProvider(Provider)
     */
    public void setProvider(final Provider<?> provider) {
        modelMapper.getConfiguration().setProvider(provider);
    }
}
