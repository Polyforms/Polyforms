package org.polyforms.repository.jpa.query;

import org.polyforms.repository.ExecutorPrefixHolder;

/**
 * Helper for creating query string of select statement from string.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class SelectQueryStringBuilder extends JpqlQueryStringBuilder {
    protected SelectQueryStringBuilder(final ExecutorPrefixHolder executorPrefix) {
        super(executorPrefix);
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("SELECT");
        if (selectClause.contains(KeyWord.Distinct.name())) {
            jpql.appendKeyWord(KeyWord.Distinct, false);
        }
        jpql.appendAlias();
        jpql.appendToken("FROM");
        jpql.appendEntity();
        jpql.appendAlias();
    }
}
