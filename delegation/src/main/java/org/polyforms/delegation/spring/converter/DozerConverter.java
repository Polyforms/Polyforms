package org.polyforms.delegation.spring.converter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang.ClassUtils;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * Adapter of Dozer {@link Mapper} for Spring {@link GenericConverter}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
@Singleton
public final class DozerConverter implements GenericConverter {
    private final Mapper dozerMapper;

    /**
     * Create an instance with Dozer {@link Mapper}.
     */
    @Inject
    public DozerConverter(final Mapper dozerMapper) {
        this.dozerMapper = dozerMapper;
    }

    /**
     * {@inheritDoc}
     */
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        Class<?> type = targetType.getType();
        if (type.isPrimitive()) {
            type = ClassUtils.primitiveToWrapper(type);
        }

        if (type.isAssignableFrom(source.getClass())) {
            return source;
        }

        return dozerMapper.map(source, type);
    }

    /**
     * Set all {@link CustomConverter} in container.
     */
    @Inject
    public void setCustomConverters(final List<CustomConverter> customConverters) {
        ((DozerBeanMapper) dozerMapper).setCustomConverters(customConverters);
    }
}
