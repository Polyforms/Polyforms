package org.polyforms.repository.spring.converter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

/**
 * Converter which converts identifier to relevant entity.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class IdentifierToEntityConverter extends EntityConverter {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an instance with {@link EntityHelper}.
     */
    @Autowired
    public IdentifierToEntityConverter(final EntityHelper entityHelper) {
        super(entityHelper);
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        return canBeConverted(targetType.getType(), sourceType.getType());
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        return entityManager.find(targetType.getType(), source);
    }
}
