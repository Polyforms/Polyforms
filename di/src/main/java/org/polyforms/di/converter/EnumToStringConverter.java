package org.polyforms.di.converter;

import java.util.Collections;
import java.util.Set;

import javax.inject.Named;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * Converter which converts {@link Enum} to {@link String}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class EnumToStringConverter implements GenericConverter {
    /**
     * {@inheritDoc}
     */
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Enum.class, String.class));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes" })
    public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        return ((Enum) source).name();
    }
}
