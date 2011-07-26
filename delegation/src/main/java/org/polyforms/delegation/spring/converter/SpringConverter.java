package org.polyforms.delegation.spring.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Provider;

import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Adapter of Spring {@link ConversionService} for ModelMapper {@link org.modelmapper.Converter}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public class SpringConverter implements ConditionalConverter<Object, Object> {
    private final Provider<ConversionService> conversionServiceProvider;

    /**
     * Create an instance with Spring {@link ConversionService}.
     */
    @Autowired
    public SpringConverter(final Provider<ConversionService> conversionServiceProvider) {
        this.conversionServiceProvider = conversionServiceProvider;
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final MappingContext<Object, Object> context) {
        final Object source = context.getSource();
        if (source == null) {
            return null;
        }
        return conversionServiceProvider.get().convert(source, TypeDescriptor.valueOf(context.getSourceType()),
                TypeDescriptor.valueOf(context.getDestinationType()));
    }

    /**
     * {@inheritDoc}
     */
    public boolean supports(final Class<?> sourceType, final Class<?> destinationType) {
        final Method method = ReflectionUtils.findMethod(GenericConversionService.class, "getConverter",
                new Class<?>[] { TypeDescriptor.class, TypeDescriptor.class });
        ReflectionUtils.makeAccessible(method);

        final GenericConverter converter = getConverter(sourceType, destinationType);
        return converter != null && !(converter instanceof ModelMapperConverter);
    }

    private GenericConverter getConverter(final Class<?> sourceType, final Class<?> destinationType) {
        final Method method = ReflectionUtils.findMethod(GenericConversionService.class, "getConverter",
                new Class<?>[] { TypeDescriptor.class, TypeDescriptor.class });
        ReflectionUtils.makeAccessible(method);
        try {
            return (GenericConverter) method.invoke(conversionServiceProvider.get(),
                    new Object[] { TypeDescriptor.valueOf(sourceType), TypeDescriptor.valueOf(destinationType) });
        } catch (final InvocationTargetException e) {
            return null;
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Should never get here");
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsSource(final Class<?> sourceType) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean verifiesSource() {
        return true;
    }
}
