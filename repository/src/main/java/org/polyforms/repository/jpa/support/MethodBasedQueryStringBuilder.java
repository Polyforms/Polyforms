package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class MethodBasedQueryStringBuilder implements QueryResolver {
    private static final String ENTITY_CLASS_PLACE_HOLDER = "{ENTITY_CLASS_HOLDER}";
    private static final String ORDER_BY = "OrderBy";
    private static final String BY = "By";
    private Map<String, String> queryStringCache = new HashMap<String, String>();
    private final String regex;

    public MethodBasedQueryStringBuilder() {
        final StringBuffer keyWords = new StringBuffer();
        for (final KeyWord keyWord : KeyWord.values()) {
            if (keyWords.length() > 0) {
                keyWords.append("|");
            }
            keyWords.append(keyWord.name());
        }
        regex = String.format("(?<=[a-z])((?<=%1$s)|(?=%1$s))", keyWords.toString());
    }

    public String getQuery(final Class<?> entityClass, final Method method) {
        final String methodName = method.getName();
        if (!queryStringCache.containsKey(methodName)) {
            final String[] parts = split(methodName);

            final IndexHolder indexHolder = new IndexHolder();
            final StringBuffer jpql = new StringBuffer();
            appendSelectClause(jpql, parts[0]);
            if (!parts[1].isEmpty()) {
                appendWhereClause(indexHolder, parts[1], jpql);
            }
            if (!parts[2].isEmpty()) {
                appendOrderClause(jpql, parts[2], indexHolder);
            }
            queryStringCache.put(methodName, jpql.toString());
        }

        return queryStringCache.get(methodName).replace(ENTITY_CLASS_PLACE_HOLDER, entityClass.getSimpleName());
    }

    private String[] split(final String methodName) {
        int orderByPosition = methodName.indexOf(ORDER_BY);
        if (orderByPosition == -1) {
            orderByPosition = methodName.length();
        }
        int byPosition = methodName.substring(0, orderByPosition).indexOf(BY);
        if (byPosition == -1) {
            byPosition = orderByPosition;
        }

        final String[] parts = new String[3];
        parts[0] = byPosition == 0 ? "" : methodName.substring(0, byPosition);
        parts[1] = byPosition == orderByPosition ? "" : methodName.substring(byPosition + BY.length(), orderByPosition);
        parts[2] = orderByPosition == methodName.length() ? "" : methodName.substring(orderByPosition
                + ORDER_BY.length());
        return parts;
    }

    private void appendSelectClause(final StringBuffer jpql, final String selectClause) {
        jpql.append("SELECT ");
        if (selectClause.contains(KeyWord.Distinct.name())) {
            appendKeyWord(jpql, KeyWord.Distinct);
        }
        jpql.append("e FROM ");
        jpql.append(ENTITY_CLASS_PLACE_HOLDER);
        jpql.append(" e ");
    }

    private void appendWhereClause(final IndexHolder indexHolder, final String whereClause, final StringBuffer jpql) {
        jpql.append("WHERE ");
        boolean not = false;
        boolean property = false;
        for (final String token : whereClause.split(regex)) {
            try {
                final KeyWord keyWord = KeyWord.valueOf(token);
                switch (keyWord) {
                    case Not:
                        not = true;
                        break;
                    case And:
                    case Or:
                        if (property) {
                            appendKeyWord(jpql, KeyWord.Equal, not, indexHolder);
                        }
                    default:
                        appendKeyWord(jpql, keyWord, not, indexHolder);
                        not = false;
                        property = false;
                }
            } catch (final IllegalArgumentException e) {
                appendProperty(jpql, token);
                property = true;
            }
        }
        if (property) {
            appendKeyWord(jpql, KeyWord.Equal, not, indexHolder);
        }
    }

    private void appendOrderClause(final StringBuffer jpql, final String orderClause, final IndexHolder indexHolder) {
        jpql.append("ORDER BY ");
        for (final String token : orderClause.split(regex)) {
            try {
                appendKeyWord(jpql, KeyWord.valueOf(token));
            } catch (final IllegalArgumentException e) {
                appendProperty(jpql, token);
            }
        }
    }

    private void appendKeyWord(final StringBuffer jpql, final KeyWord keyWord) {
        jpql.append(keyWord.getToken());
        jpql.append(" ");
    }

    private void appendKeyWord(final StringBuffer jpql, final KeyWord keyWord, boolean not,
            final IndexHolder indexHolder) {
        jpql.append(keyWord.getToken(not, indexHolder));
        jpql.append(" ");
    }

    private void appendProperty(final StringBuffer jpql, final String property) {
        jpql.append("e.");
        jpql.append(property.toLowerCase());
        jpql.append(" ");
    }
}

interface Token {
    String getToken(boolean not, IndexHolder indexHoder);
}

class IndexHolder {
    private int index = 1;

    public int get() {
        return index++;
    }
}

enum KeyWord implements Token {
    Distinct, And, Or, Not, Between("BETWEEN {} AND {}"), Is, Null("NULL"), Empty("EMPTY"), Member("MEMBER"), Of, LessThan(
            "< {}", ">= {}"), GreatThan("> {}", "<= {}"), Equal("= {}", "<> {}"), Like("LIKE {}"), In("IN {}"), Asc, Desc;

    private static final String POSITIONAL_PARAMETER_PLACE_HOLDER = "{}";
    private String token;
    private String notToken;

    private KeyWord(final String token, final String notToken) {
        this.token = token;
        this.notToken = notToken;
    }

    private KeyWord(final String token) {
        this(token, "NOT " + token);
    }

    private KeyWord() {
    }

    public String getToken(final boolean not, final IndexHolder indexHoder) {
        final String result = not ? getNotToken() : getToken();
        return replacePositionalParameters(result, indexHoder);
    }

    private String replacePositionalParameters(final String result, final IndexHolder indexHoder) {
        final StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (true) {
            final int j = result.indexOf(POSITIONAL_PARAMETER_PLACE_HOLDER, i);
            if (j == -1) {
                break;
            }

            buffer.append(result.substring(i, j));
            buffer.append("?" + indexHoder.get());
            i = j + POSITIONAL_PARAMETER_PLACE_HOLDER.length();
        }
        buffer.append(result.substring(i));
        return buffer.toString();
    }

    protected String getToken() {
        if (token == null) {
            token = name().toUpperCase();
        }
        return token;
    }

    private String getNotToken() {
        if (notToken == null) {
            notToken = name().toUpperCase();
        }
        return notToken;
    }
}
