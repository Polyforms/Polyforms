package org.polyforms.repository.jpa.support;

import org.polyforms.repository.ExecutorPrefix;

class DeleteQueryStringBuilder extends JpqlQueryStringBuilder {
    protected DeleteQueryStringBuilder(final ExecutorPrefix executorPrefix) {
        super(executorPrefix);
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("DELETE FROM");
        jpql.appendToken(ENTITY_CLASS_PLACE_HOLDER);
        jpql.appendToken("e");
    }
}
