package org.polyforms.repository.spring.converter;

import java.util.Set;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

public class EntityConverterTest {
    private EntityHelper entityHelper;
    private EntityConverter converter;

    @Before
    public void setUp() {
        entityHelper = EasyMock.createMock(EntityHelper.class);
        converter = new EntityConverter(entityHelper) {
            public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
                return null;
            }

            public boolean matches(final TypeDescriptor sourceType, final TypeDescriptor targetType) {
                return false;
            }
        };
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
        entityHelper.isEntity(Object.class);
        EasyMock.expectLastCall().andReturn(true);
        entityHelper.getIdentifierClass(Object.class);
        EasyMock.expectLastCall().andReturn(Long.class);
        EasyMock.replay(entityHelper);

        Assert.assertTrue(converter.canBeConverted(Object.class, Long.class));
        EasyMock.verify(entityHelper);
    }

    @Test
    public void notMatches() {
        entityHelper.isEntity(Object.class);
        EasyMock.expectLastCall().andReturn(true);
        entityHelper.getIdentifierClass(Object.class);
        EasyMock.expectLastCall().andReturn(Integer.class);
        EasyMock.replay(entityHelper);

        Assert.assertFalse(converter.canBeConverted(Object.class, Long.class));
        EasyMock.verify(entityHelper);
    }

    @Test
    public void notEntity() {
        entityHelper.isEntity(Object.class);
        EasyMock.expectLastCall().andReturn(false);
        EasyMock.replay(entityHelper);

        Assert.assertFalse(converter.canBeConverted(Object.class, Long.class));
        EasyMock.verify(entityHelper);
    }
}
