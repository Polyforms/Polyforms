package org.polyforms.repository.jpa.support;

import org.polyforms.repository.ExecutorPrefix;

class SelectQueryStringBuilder extends JpqlQueryStringBuilder {
    protected SelectQueryStringBuilder(final ExecutorPrefix executorPrefix) {
        super(executorPrefix);
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("SELECT");
        if (selectClause.contains(KeyWord.Distinct.name())) {
            jpql.appendKeyWord(KeyWord.Distinct, false);
        }
        jpql.appendToken("e FROM");
        jpql.appendToken(ENTITY_CLASS_PLACE_HOLDER);
        jpql.appendToken("e");
    }
}
