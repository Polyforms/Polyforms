package org.polyforms.repository.jpa.support;

class DeleteQueryStringBuilder extends JpqlQueryStringBuilder {
    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("DELETE FROM");
        jpql.appendToken(ENTITY_CLASS_PLACE_HOLDER);
        jpql.appendToken("e");
    }
}
