package org.polyforms.repository.converter;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.test.util.ReflectionTestUtils;

public class IdentifierToEntityConverterTest {
    private EntityManager entityManager;
    private EntityHelper entityHelper;
    private ConditionalGenericConverter converter;

    @Before
    public void setUp() {
        entityHelper = EasyMock.createMock(EntityHelper.class);
        converter = new IdentifierToEntityConverter(entityHelper);

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(converter, "entityManager", entityManager);
    }

    @Test
    public void convert() {
        final String entity = new String();

        entityManager.find(String.class, 1L);
        EasyMock.expectLastCall().andReturn(entity);
        EasyMock.replay(entityManager);

        Assert.assertSame(entity, converter.convert(1L, null, TypeDescriptor.valueOf(String.class)));
        EasyMock.verify(entityManager);
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

        Assert.assertTrue(converter.matches(TypeDescriptor.valueOf(Long.class), TypeDescriptor.valueOf(Object.class)));
        EasyMock.verify(entityHelper);
    }
}
