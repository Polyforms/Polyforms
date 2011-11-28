package org.polyforms.repository.jpa.query;

import org.polyforms.repository.ExecutorPrefixHolder;

/**
 * Helper for creating query string of delete statement from string.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class DeleteQueryStringBuilder extends JpqlQueryStringBuilder {
    protected DeleteQueryStringBuilder(final ExecutorPrefixHolder executorPrefix) {
        super(executorPrefix);
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("DELETE FROM");
        jpql.appendEntity();
        jpql.appendAlias();
    }
}
