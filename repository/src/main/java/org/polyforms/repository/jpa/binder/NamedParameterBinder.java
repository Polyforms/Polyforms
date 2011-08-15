package org.polyforms.repository.jpa.binder;

import javax.persistence.Parameter;
import javax.persistence.Query;

class NamedParameterBinder extends AbstractParameterBinder<String> {
    public NamedParameterBinder() {
        super();
        addParameterMatcher(new NamedParameterMatcher());
    }

    @Override
    protected void setParameter(final Query query, final String key, final Object value) {
        query.setParameter(key, value);
    }

    public String getKey(final Parameter<?> parameter) {
        return parameter.getName();
    }
}
