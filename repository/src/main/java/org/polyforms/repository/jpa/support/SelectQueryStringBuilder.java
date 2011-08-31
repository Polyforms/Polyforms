package org.polyforms.repository.jpa.support;

class SelectQueryStringBuilder extends JpqlQueryStringBuilder {
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
