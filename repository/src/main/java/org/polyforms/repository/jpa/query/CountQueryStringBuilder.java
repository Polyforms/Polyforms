package org.polyforms.repository.jpa.query;

import org.polyforms.repository.ExecutorPrefixHolder;

/**
 * Helper for creating query string of count statement from string.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class CountQueryStringBuilder extends JpqlQueryStringBuilder {
    protected CountQueryStringBuilder(final ExecutorPrefixHolder executorPrefix) {
        super(executorPrefix);
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("SELECT count(");
        if (selectClause.contains(KeyWord.Distinct.name())) {
            jpql.appendKeyWord(KeyWord.Distinct, false);
        }
        jpql.appendAlias();
        jpql.appendToken(") FROM");
        jpql.appendEntity();
        jpql.appendAlias();
    }
}
