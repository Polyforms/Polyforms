package org.polyforms.di.spring.converter;

import java.lang.annotation.ElementType;
import java.util.Set;

import org.junit.Assert;

import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public class StringToEnumConverterTest {
    private final GenericConverter converter = new StringToEnumConverter();

    @Test
    public void getConvertibleTypes() {
        final Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        Assert.assertEquals(1, convertibleTypes.size());

        final ConvertiblePair convertiblePair = convertibleTypes.iterator().next();
        Assert.assertSame(String.class, convertiblePair.getSourceType());
        Assert.assertSame(Enum.class, convertiblePair.getTargetType());
    }

    @Test
    public void convert() {
        Assert.assertEquals(ElementType.METHOD,
                converter.convert("METHOD", null, TypeDescriptor.valueOf(ElementType.class)));
    }

    @Test
    public void convertNull() {
        Assert.assertNull(converter.convert(null, null, null));
    }
}
