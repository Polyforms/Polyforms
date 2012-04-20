package org.polyforms.di.converter;

import java.lang.annotation.ElementType;
import java.util.Set;

import org.dozer.Mapper;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public class DozerConverterTest {
    private Mapper beanMapper;
    private GenericConverter converter;

    @Before
    public void setUp() {
        beanMapper = EasyMock.createMock(Mapper.class);
        converter = new DozerConverter(beanMapper);
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
    public void convert() {
        beanMapper.map("METHOD", ElementType.class);
        EasyMock.expectLastCall().andReturn(ElementType.METHOD);
        EasyMock.replay(beanMapper);

        Assert.assertEquals(ElementType.METHOD,
                converter.convert("METHOD", null, TypeDescriptor.valueOf(ElementType.class)));
        EasyMock.verify(beanMapper);
    }

    @Test
    public void convertNull() {
        Assert.assertNull(converter.convert(null, null, null));
    }
}
