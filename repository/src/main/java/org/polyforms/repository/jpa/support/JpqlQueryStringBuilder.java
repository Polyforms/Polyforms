package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

class JpqlQueryStringBuilder implements QueryResolver {
    private static final int NUMBER_OF_PARTS = 3;
    private static final String EMPTY_STRING = "";
    private static final String ENTITY_CLASS_PLACE_HOLDER = "{ENTITY_CLASS_HOLDER}";
    private static final String ORDER_BY = "OrderBy";
    private static final String BY = "By";
    private final Map<String, String> queryStringCache = new HashMap<String, String>();
    private final String regex;

    public JpqlQueryStringBuilder() {
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
        final String methodName = StringUtils.capitalize(method.getName());
        if (!queryStringCache.containsKey(methodName)) {
            final String[] parts = split(methodName);

            final JpqlStringBuffer jpql = new JpqlStringBuffer();
            appendSelectClause(jpql, parts[0]);
            if (StringUtils.hasText(parts[1])) {
                appendWhereClause(jpql, parts[1]);
            }
            if (StringUtils.hasText(parts[2])) {
                appendOrderClause(jpql, parts[2]);
            }
            queryStringCache.put(methodName, jpql.getJpql());
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

        final String[] parts = new String[NUMBER_OF_PARTS];
        parts[0] = byPosition == 0 ? EMPTY_STRING : methodName.substring(0, byPosition);
        parts[1] = byPosition == orderByPosition ? EMPTY_STRING : methodName.substring(byPosition + BY.length(),
                orderByPosition);
        parts[2] = orderByPosition == methodName.length() ? EMPTY_STRING : methodName.substring(orderByPosition
                + ORDER_BY.length());
        return parts;
    }

    private void appendSelectClause(final JpqlStringBuffer jpql, final String selectClause) {
        jpql.appendToken("SELECT");
        if (selectClause.contains(KeyWord.Distinct.name())) {
            jpql.appendKeyWord(KeyWord.Distinct, false);
        }
        jpql.appendToken("e FROM");
        jpql.appendToken(ENTITY_CLASS_PLACE_HOLDER);
        jpql.appendToken("e");
    }

    private void appendWhereClause(final JpqlStringBuffer jpql, final String whereClause) {
        jpql.appendToken("WHERE");
        boolean not = false;
        for (final String token : whereClause.split(regex)) {
            try {
                final KeyWord keyWord = KeyWord.valueOf(token);
                if (KeyWord.Not == keyWord) {
                    not = true;
                    continue;
                }
                if (KeyWord.LOGICAL_OPERATORS.contains(keyWord)) {
                    jpql.appendEqualsIfNecessary();
                }
                if (KeyWord.OPERATORS.contains(keyWord)) {
                    jpql.appendProperty();
                }
                jpql.appendKeyWord(keyWord, not);
                if (KeyWord.NOT_CONSUMERS.contains(keyWord)) {
                    not = false;
                }
            } catch (final IllegalArgumentException e) {
                jpql.newProperty(token);
            }
        }
        jpql.appendEqualsIfNecessary();
    }

    private void appendOrderClause(final JpqlStringBuffer jpql, final String orderClause) {
        jpql.appendToken("ORDER BY");
        for (final String token : orderClause.split(regex)) {
            try {
                jpql.appendKeyWord(KeyWord.valueOf(token), false);
            } catch (final IllegalArgumentException e) {
                jpql.newProperty(token);
                jpql.appendProperty();
            }
        }
    }

}

class JpqlStringBuffer {
    private final IndexHolder indexHolder = new IndexHolder();
    private final StringBuffer jpql = new StringBuffer();
    private boolean newProperty;
    private String lastProperty;

    public void appendKeyWord(final KeyWord keyWord, final boolean not) {
        appendToken(keyWord.getToken(not, indexHolder));
        newProperty = false;
    }

    public void appendEqualsIfNecessary() {
        if (newProperty) {
            appendProperty();
            appendKeyWord(KeyWord.Equal, false);
        }
    }

    public void newProperty(final String property) {
        lastProperty = property;
        newProperty = true;
    }

    public void appendProperty() {
        jpql.append("e.");
        appendToken(StringUtils.uncapitalize(lastProperty));
    }

    public void appendToken(final String token) {
        jpql.append(token);
        jpql.append(" ");
    }

    public String getJpql() {
        return jpql.toString();
    }
}

class IndexHolder {
    private int index = 1;

    public int get() {
        return index++;
    }
}

enum KeyWord {
    Distinct, And, Or, Not, Between("BETWEEN {} AND {}"), Is, Null("NULL"), Empty("EMPTY"), Member("MEMBER"), Of, LessThan(
            "< {}", ">= {}"), GreatThan("> {}", "<= {}"), Equal("= {}", "<> {}"), Like("LIKE {}"), In("IN {}"), Asc, Desc;

    public static final List<KeyWord> LOGICAL_OPERATORS = new ArrayList<KeyWord>();
    public static final List<KeyWord> OPERATORS = new ArrayList<KeyWord>();
    public static final List<KeyWord> NOT_CONSUMERS = new ArrayList<KeyWord>();
    private static final String POSITIONAL_PARAMETER_PLACE_HOLDER = "{}";
    private String token;
    private String notToken;

    static {
        LOGICAL_OPERATORS.add(KeyWord.And);
        LOGICAL_OPERATORS.add(KeyWord.Or);

        OPERATORS.add(KeyWord.Equal);
        OPERATORS.add(KeyWord.LessThan);
        OPERATORS.add(KeyWord.GreatThan);
        OPERATORS.add(KeyWord.Is);
        OPERATORS.add(KeyWord.Like);
        OPERATORS.add(KeyWord.In);
        OPERATORS.add(KeyWord.Between);
        OPERATORS.add(KeyWord.Member);

        NOT_CONSUMERS.add(KeyWord.Equal);
        NOT_CONSUMERS.add(KeyWord.LessThan);
        NOT_CONSUMERS.add(KeyWord.GreatThan);
        NOT_CONSUMERS.add(KeyWord.Null);
        NOT_CONSUMERS.add(KeyWord.Empty);
        NOT_CONSUMERS.add(KeyWord.Like);
        NOT_CONSUMERS.add(KeyWord.In);
        NOT_CONSUMERS.add(KeyWord.Between);
        NOT_CONSUMERS.add(KeyWord.Member);
    }

    private KeyWord(final String token, final String notToken) {
        this.token = token;
        this.notToken = notToken;
    }

    private KeyWord(final String token) {
        this(token, "NOT " + token);
    }

    private KeyWord() {
        token = name().toUpperCase(Locale.getDefault());
        notToken = token;
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
            buffer.append("?");
            buffer.append(indexHoder.get());
            i = j + POSITIONAL_PARAMETER_PLACE_HOLDER.length();
        }
        buffer.append(result.substring(i));
        return buffer.toString();
    }

    private String getToken() {
        return token;
    }

    private String getNotToken() {
        return notToken;
    }
}
