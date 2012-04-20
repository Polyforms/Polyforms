package org.polyforms.di.converter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.ClassUtils;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * Adapter of Dozer {@link Mapper} for Spring
 * {@link org.springframework.core.convert.converter.GenericConverter}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class DozerConverter implements GenericConverter {
	private final Mapper beanMapper;

	/**
	 * Create an instance with {@link Mapper}.
	 */
	@Inject
	public DozerConverter(final Mapper beanMapper) {
		this.beanMapper = beanMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object.class,
				Object.class));
	}

	/**
	 * {@inheritDoc}
	 */
	public Object convert(final Object source, final TypeDescriptor sourceType,
			final TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}

		Class<?> type = targetType.getType();
		if (type.isPrimitive()) {
			type = ClassUtils.primitiveToWrapper(type);
		}

		if (type.isAssignableFrom(source.getClass())) {
			return source;
		}

		return beanMapper.map(source, type);
	}

	/**
	 * Set all {@link CustomConverter} in container.
	 */
	@Inject
	public void setCustomConverters(final List<CustomConverter> customConverters) {
		((DozerBeanMapper) beanMapper).setCustomConverters(customConverters);
	}
}
