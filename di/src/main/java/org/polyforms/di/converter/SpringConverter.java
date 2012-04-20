package org.polyforms.di.converter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.dozer.CustomConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Adapter of Spring {@link ConversionService} for ModelMapper
 * {@link org.modelmapper.Converter}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class SpringConverter implements CustomConverter {
	private final Provider<ConversionService> conversionServiceProvider;

	/**
	 * Create an instance with Spring {@link ConversionService}.
	 */
	@Inject
	public SpringConverter(
			final Provider<ConversionService> conversionServiceProvider) {
		this.conversionServiceProvider = conversionServiceProvider;
	}

	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		if (sourceFieldValue == null) {
			return null;
		}
		return conversionServiceProvider.get().convert(sourceFieldValue,
				TypeDescriptor.valueOf(sourceClass),
				TypeDescriptor.valueOf(destinationClass));
	}
}
