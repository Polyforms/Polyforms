package org.polyforms.di.converter;

import java.lang.annotation.ElementType;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public class EnumToStringConverterTest {
    private final GenericConverter converter = new EnumToStringConverter();

    @Test
    public void getConvertibleTypes() {
        final Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        Assert.assertEquals(1, convertibleTypes.size());

        final ConvertiblePair convertiblePair = convertibleTypes.iterator().next();
        Assert.assertSame(Enum.class, convertiblePair.getSourceType());
        Assert.assertSame(String.class, convertiblePair.getTargetType());
    }

    @Test
    public void convert() {
        Assert.assertEquals("METHOD", converter.convert(ElementType.METHOD, null, null));
    }

    @Test
    public void convertNull() {
        Assert.assertNull(converter.convert(null, null, null));
    }
}
