package org.polyforms.util;

import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConversionService;

public class ConversionUtilsTest implements ForConversionTest<String> {
    private ConversionService conversionService;
    private Method method;

    @Before
    public void setUp() throws NoSuchMethodException {
        conversionService = EasyMock.createMock(ConversionService.class);
        method = this.getClass().getMethod("indexOf", new Class<?>[] { String[].class, Integer.class });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new ConversionUtils();
    }

    @Test
    public void convertArguments() {
        final Integer[] integers = new Integer[] { 1 };
        Object[] arguments = new Object[] { integers, 0 };

        conversionService.convert(integers, String[].class);
        EasyMock.expectLastCall().andReturn(new String[] { "1" });
        EasyMock.replay(conversionService);

        Object[] convertedArguments = ConversionUtils.convertArguments(conversionService, this.getClass(), method,
                arguments);
        Assert.assertEquals("1", ((String[]) convertedArguments[0])[0]);
        Assert.assertEquals(0, convertedArguments[1]);
        EasyMock.verify(conversionService);
    }

    @Test
    public void convertReturnValue() {
        conversionService.convert(1, String.class);
        EasyMock.expectLastCall().andReturn("1");
        EasyMock.replay(conversionService);

        Assert.assertEquals("1", ConversionUtils.convertReturnValue(conversionService, this.getClass(), method, 1));
        EasyMock.verify(conversionService);
    }

    @Test
    public void returnValueDirectly() {
        Assert.assertEquals("1", ConversionUtils.convertReturnValue(conversionService, this.getClass(), method, "1"));
    }

    @Test
    public void returnNull() {
        Assert.assertNull(ConversionUtils.convertReturnValue(conversionService, this.getClass(), method, null));
    }

    public String indexOf(String[] values, Integer index) {
        return null;
    }
}

interface ForConversionTest<T> {
    T indexOf(T[] values, Integer index);
}
