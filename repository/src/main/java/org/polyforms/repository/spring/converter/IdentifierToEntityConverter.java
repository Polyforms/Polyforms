package org.polyforms.repository.spring.converter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Converter which converts identifier to relevant entity.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class IdentifierToEntityConverter extends EntityConverter {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
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
        return entityManager.find(targetType.getType(), source);
    }
}
