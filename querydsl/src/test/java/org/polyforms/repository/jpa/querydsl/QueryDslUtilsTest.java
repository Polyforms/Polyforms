package org.polyforms.repository.jpa.querydsl;

import org.junit.Assert;
import org.junit.Test;

public class QueryDslUtilsTest {
    @Test(expected = UnsupportedOperationException.class)
    public void cannotInstance() {
        new QueryDslUtils();
    }

    @Test
    public void findEntityPath() {
        Assert.assertSame(QMockEntity.mockEntity, QueryDslUtils.findEntityPath(MockEntity.class));
        // Just for testing cache
        Assert.assertSame(QMockEntity.mockEntity, QueryDslUtils.findEntityPath(MockEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findEntityPathForInexistedClass() {
        QueryDslUtils.findEntityPath(String.class);
    }

    @Test(expected = IllegalStateException.class)
    public void findEntityPathWithStaticField() {
        QueryDslUtils.findEntityPath(IncompleteEntity.class);
    }
}
