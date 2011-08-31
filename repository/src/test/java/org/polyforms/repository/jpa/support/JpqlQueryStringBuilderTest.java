package org.polyforms.repository.jpa.support;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.polyforms.repository.jpa.EntityHelper;

public class JpqlQueryStringBuilderTest {
    @Test
    public void byName() {
        final JpqlQueryStringBuilder jpqlQueryStringBuilder = new SelectQueryStringBuilder();
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.name = ?1 ",
                jpqlQueryStringBuilder.getQuery(EntityClass.class, "byName"));
        // Just for testing cache
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.name = ?1 ",
                jpqlQueryStringBuilder.getQuery(EntityClass.class, "byName"));
    }

    @Test
    public void getDistinctByNameAndCodeIn() {
        Assert.assertEquals(
                "SELECT DISTINCT e FROM EntityClass e WHERE e.name = ?1 AND e.code IN ?2 ORDER BY e.id DESC ",
                new SelectQueryStringBuilder().getQuery(EntityClass.class, "DistinctByNameAndCodeInOrderByIdDesc"));
    }

    @Test
    public void orderById() {
        Assert.assertEquals("SELECT e FROM EntityClass e ORDER BY e.id ",
                new SelectQueryStringBuilder().getQuery(EntityClass.class, "orderById"));
    }

    @Test
    public void countByUser_NameBetween() {
        final EntityHelper entityHelper = EasyMock.createMock(EntityHelper.class);
        entityHelper.getIdentifierName(EntityClass.class);
        EasyMock.expectLastCall().andReturn("id");
        EasyMock.replay(entityHelper);

        Assert.assertEquals("SELECT count( e.id ) FROM EntityClass e WHERE e.user.name BETWEEN ?1 AND ?2 ",
                new CountQueryStringBuilder(entityHelper).getQuery(EntityClass.class, "ByUser_NameBetween"));
        EasyMock.verify(entityHelper);
    }

    @Test
    public void countByUserNameOrderByIdDesc() {
        final EntityHelper entityHelper = EasyMock.createMock(EntityHelper.class);
        entityHelper.getIdentifierName(EntityClass.class);
        EasyMock.expectLastCall().andReturn("id");
        EasyMock.replay(entityHelper);

        Assert.assertEquals("SELECT count( DISTINCT e.id ) FROM EntityClass e WHERE e.userName = ?1 ",
                new CountQueryStringBuilder(entityHelper).getQuery(EntityClass.class, "DistinctByUserName"));
        EasyMock.verify(entityHelper);
    }

    @Test
    public void updateCodeAndNameByUserAgeIsNotNullOrNotGreatThan() {
        Assert.assertEquals(
                "UPDATE EntityClass e SET e.code = ?1 , e.name = ?2 WHERE e.user.age IS NOT NULL OR e.user.age <= ?3 ",
                new UpdateQueryStringBuilder().getQuery(EntityClass.class,
                        "CodeAndNameByUserAgeIsNotNullOrNotGreatThan"));
    }

    @Test
    public void deleteByName() {
        Assert.assertEquals("DELETE FROM EntityClass e WHERE e.name = ?1 ",
                new DeleteQueryStringBuilder().getQuery(EntityClass.class, "byName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByUserFirstName() {
        new SelectQueryStringBuilder().getQuery(EntityClass.class, "ByUserFirstName");
    }

    @SuppressWarnings("unused")
    private static class EntityClass {
        private Integer id;

        private User user;

        private String code;

        private String name;

        private String userName;
    }

    @SuppressWarnings("unused")
    private static class User {
        private String name;
        private String age;
    }
}
