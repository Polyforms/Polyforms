package org.polyforms.repository.jpa.binder;

import javax.persistence.Parameter;
import javax.persistence.Query;

/**
 * Binder for named parameters of JPA.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class NamedParameterBinder extends AbstractParameterBinder<String> {
    protected NamedParameterBinder() {
        super();
        addParameterMatcher(new NamedParameterMatcher());
    }

    @Override
    protected void setParameter(final Query query, final String key, final Object value) {
        query.setParameter(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public String getKey(final Parameter<?> parameter) {
        return parameter.getName();
    }
}
