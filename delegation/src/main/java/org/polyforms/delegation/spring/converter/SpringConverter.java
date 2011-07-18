package org.polyforms.delegation.spring.converter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.dozer.CustomConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Adapter of Spring {@link ConversionService} for Dozer {@link CustomConverter}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
@Singleton
public final class SpringConverter implements CustomConverter {
    private final Provider<ConversionService> conversionServiceProvider;

    /**
     * Create an instance with Spring {@link ConversionService}.
     */
    @Inject
    public SpringConverter(final Provider<ConversionService> conversionServiceProvider) {
        this.conversionServiceProvider = conversionServiceProvider;
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object destination, final Object source, final Class<?> destinationClass,
            final Class<?> sourceClass) {
        return conversionServiceProvider.get().convert(source, TypeDescriptor.valueOf(sourceClass),
                TypeDescriptor.valueOf(destinationClass));
    }
}
