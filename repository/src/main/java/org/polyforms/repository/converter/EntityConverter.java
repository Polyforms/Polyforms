package org.polyforms.repository.converter;

import java.util.Collections;
import java.util.Set;

import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * Abstract class of converter for entity.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
abstract class EntityConverter implements ConditionalGenericConverter {
    private final EntityHelper entityHelper;

    protected EntityConverter(final EntityHelper entityHelper) {
        this.entityHelper = entityHelper;
    }

    /**
     * {@inheritDoc}
     */
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    protected boolean canBeConverted(final Class<?> entityClass, final Class<?> identifierClass) {
        return entityHelper.isEntity(entityClass) && entityHelper.getIdentifierClass(entityClass) == identifierClass;
    }

    protected EntityHelper getEntityHelper() {
        return entityHelper;
    }
}
