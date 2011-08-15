package org.polyforms.repository.jpa.binder;

import javax.persistence.Parameter;
import javax.persistence.Query;

class PositionalParameterBinder extends AbstractParameterBinder<Integer> {
    public PositionalParameterBinder() {
        super();
        addParameterMatcher(new PositionalParameterMatcher());
    }

    @Override
    protected void setParameter(final Query query, final Integer key, final Object value) {
        query.setParameter(key, value);
    }

    public Integer getKey(final Parameter<?> parameter) {
        return parameter.getPosition();
    }
}
