package org.polyforms.repository.jpa.support;

class UpdateQueryStringBuilder extends JpqlQueryStringBuilder {
    @Override
    protected void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("UPDATE");
        jpql.appendToken(ENTITY_CLASS_PLACE_HOLDER);
        jpql.appendToken("e");

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
