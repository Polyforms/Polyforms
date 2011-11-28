package org.polyforms.repository.jpa.query;

import org.polyforms.repository.ExecutorPrefixHolder;

/**
 * Helper for creating query string of update statement from string.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class UpdateQueryStringBuilder extends JpqlQueryStringBuilder {
    protected UpdateQueryStringBuilder(final ExecutorPrefixHolder executorPrefix) {
        super(executorPrefix);
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("UPDATE");
        jpql.appendEntity();
        jpql.appendAlias();

        boolean firstProperty = true;
        for (final String token : PATTERN.split(selectClause)) {
            try {
                KeyWord.valueOf(token);
                jpql.appendToken(",");
            } catch (final IllegalArgumentException e) {
                if (firstProperty) {
                    jpql.appendToken("SET");
                    firstProperty = false;
                }
                jpql.newProperty(token);
                jpql.appendEqualsIfNecessary();
            }
        }
    }
}
