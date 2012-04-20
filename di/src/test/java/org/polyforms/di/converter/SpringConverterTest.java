package org.polyforms.di.converter;

import java.lang.annotation.ElementType;

import javax.inject.Provider;

import org.dozer.CustomConverter;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

public class SpringConverterTest {
    private Provider<ConversionService> provider;
    private CustomConverter converter;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        provider = EasyMock.createMock(Provider.class);
        converter = new SpringConverter(provider);
    }

    @Test
    public void convertNull() {
        Assert.assertNull(converter.convert(null, null, Object.class, String.class));
    }

    @Test
    public void convert() {
        final ConversionService conversionService = EasyMock.createMock(ConversionService.class);

        provider.get();
        EasyMock.expectLastCall().andReturn(conversionService);
        conversionService.convert("METHOD", TypeDescriptor.valueOf(String.class),
                TypeDescriptor.valueOf(ElementType.class));
        EasyMock.expectLastCall().andReturn(ElementType.METHOD);
        EasyMock.replay(conversionService, provider);

        Assert.assertSame(ElementType.METHOD, converter.convert(null, "METHOD", ElementType.class, String.class));
        EasyMock.verify(conversionService, provider);
    }
}
