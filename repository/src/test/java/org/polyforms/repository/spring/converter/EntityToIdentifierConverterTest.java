package org.polyforms.repository.spring.converter;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public class EntityToIdentifierConverterTest {
    private EntityHelper entityHelper;
    private ConditionalGenericConverter converter;

    @Before
    public void setUp() {
        entityHelper = EasyMock.createMock(EntityHelper.class);
        converter = new EntityToIdentifierConverter(entityHelper);
    }

    @Test
    public void convert() {
        final Object entity = new Object();

        entityHelper.getIdentifierValue(entity);
        EasyMock.expectLastCall().andReturn(1L);
        EasyMock.replay(entityHelper);

        Assert.assertEquals(1L, converter.convert(entity, null, null));
        EasyMock.verify(entityHelper);
    }

    @Test
    public void convertNull() {
        Assert.assertNull(converter.convert(null, null, null));
    }

    @Test
    public void matches() {
        entityHelper.isEntity(Object.class);
        EasyMock.expectLastCall().andReturn(true);
        entityHelper.getIdentifierClass(Object.class);
        EasyMock.expectLastCall().andReturn(Long.class);
        EasyMock.replay(entityHelper);

        Assert.assertTrue(converter.matches(TypeDescriptor.valueOf(Object.class), TypeDescriptor.valueOf(Long.class)));
        EasyMock.verify(entityHelper);
    }
}
