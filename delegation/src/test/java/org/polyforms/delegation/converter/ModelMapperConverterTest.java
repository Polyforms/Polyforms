package org.polyforms.delegation.converter;

import java.lang.annotation.ElementType;
import java.util.Locale;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public class ModelMapperConverterTest {
    private ModelMapper modelMapper;
    private ConditionalGenericConverter converter;

    @Before
    public void setUp() {
        modelMapper = EasyMock.createMock(ModelMapper.class);
        converter = new ModelMapperConverter(modelMapper);
    }

    @Test
    public void getConvertibleTypes() {
        final Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        Assert.assertEquals(1, convertibleTypes.size());

        final ConvertiblePair convertiblePair = convertibleTypes.iterator().next();
        Assert.assertSame(Object.class, convertiblePair.getSourceType());
        Assert.assertSame(Object.class, convertiblePair.getTargetType());
    }

    @Test
    public void matches() {
        Assert.assertTrue(converter.matches(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Locale.class)));
    }

    @Test
    public void notMatches() {
        Assert.assertFalse(converter.matches(TypeDescriptor.valueOf(Boolean.class),
                TypeDescriptor.valueOf(boolean.class)));
    }

    @Test
    public void convert() {
        modelMapper.map("METHOD", ElementType.class);
        EasyMock.expectLastCall().andReturn(ElementType.METHOD);
        EasyMock.replay(modelMapper);

        Assert.assertEquals(ElementType.METHOD,
                converter.convert("METHOD", null, TypeDescriptor.valueOf(ElementType.class)));
        EasyMock.verify(modelMapper);
    }

    @Test
    public void convertNull() {
        Assert.assertNull(converter.convert(null, null, null));
    }
}
