package org.polyforms.repository.jpa.query;

import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.jpa.EntityHelper;

/**
 * Helper for creating query string of count statement from string.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class CountQueryStringBuilder extends JpqlQueryStringBuilder {
    protected static final String ENTITY_ID_PLACE_HOLDER = "{ENTITY_ID_PLACE_HOLDER}";
    private final EntityHelper entityHelper;

    protected CountQueryStringBuilder(final ExecutorPrefixHolder executorPrefix, final EntityHelper entityHelper) {
        super(executorPrefix);
        this.entityHelper = entityHelper;
    }

    @Override
    protected String getQuery(final Class<?> entityClass, final String queryString) {
        final String query = super.getQuery(entityClass, queryString);
        return query.replace(ENTITY_ID_PLACE_HOLDER, entityHelper.getIdentifierName(entityClass));
    }

    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("SELECT count(");
        if (selectClause.contains(KeyWord.Distinct.name())) {
            jpql.appendKeyWord(KeyWord.Distinct, false);
        }
        jpql.appendToken("e." + ENTITY_ID_PLACE_HOLDER);
        jpql.appendToken(") FROM");
        jpql.appendToken(ENTITY_CLASS_PLACE_HOLDER);
        jpql.appendToken("e");
    }
}
