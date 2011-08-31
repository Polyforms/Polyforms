package org.polyforms.repository.jpa.support;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.jpa.EntityHelper;

public class JpqlQueryStringBuilderTest {
    private ExecutorPrefix executorPrefix;

    @Before
    public void setUp() {
        executorPrefix = EasyMock.createMock(ExecutorPrefix.class);
    }

    @Test
    public void byName() {
        executorPrefix.removePrefixifAvailable("byName");
        EasyMock.expectLastCall().andReturn("byName");
        EasyMock.replay(executorPrefix);

        final JpqlQueryStringBuilder jpqlQueryStringBuilder = new SelectQueryStringBuilder(executorPrefix);
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.name = ?1 ",
                jpqlQueryStringBuilder.getQuery(EntityClass.class, "byName"));
        // Just for testing cache
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.name = ?1 ",
                jpqlQueryStringBuilder.getQuery(EntityClass.class, "byName"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void getDistinctByNameAndCodeIn() {
        executorPrefix.removePrefixifAvailable("getDistinctByNameAndCodeInOrderByIdDesc");
        EasyMock.expectLastCall().andReturn("DistinctByNameAndCodeInOrderByIdDesc");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals(
                "SELECT DISTINCT e FROM EntityClass e WHERE e.name = ?1 AND e.code IN ?2 ORDER BY e.id DESC ",
                new SelectQueryStringBuilder(executorPrefix).getQuery(EntityClass.class,
                        "getDistinctByNameAndCodeInOrderByIdDesc"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void orderById() {
        executorPrefix.removePrefixifAvailable("orderById");
        EasyMock.expectLastCall().andReturn("orderById");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals("SELECT e FROM EntityClass e ORDER BY e.id ",
                new SelectQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "orderById"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void countByUser_NameBetween() {
        final EntityHelper entityHelper = EasyMock.createMock(EntityHelper.class);
        entityHelper.getIdentifierName(EntityClass.class);
        EasyMock.expectLastCall().andReturn("id");
        executorPrefix.removePrefixifAvailable("findByUser_NameBetween");
        EasyMock.expectLastCall().andReturn("ByUser_NameBetween");
        EasyMock.replay(executorPrefix, entityHelper);

        Assert.assertEquals("SELECT count( e.id ) FROM EntityClass e WHERE e.user.name BETWEEN ?1 AND ?2 ",
                new CountQueryStringBuilder(executorPrefix, entityHelper).getQuery(EntityClass.class,
                        "findByUser_NameBetween"));
        EasyMock.verify(executorPrefix, entityHelper);
    }

    @Test
    public void countByUserNameOrderByIdDesc() {
        final EntityHelper entityHelper = EasyMock.createMock(EntityHelper.class);
        entityHelper.getIdentifierName(EntityClass.class);
        EasyMock.expectLastCall().andReturn("id");
        executorPrefix.removePrefixifAvailable("findDistinctByUserName");
        EasyMock.expectLastCall().andReturn("findDistinctByUserName");
        EasyMock.replay(executorPrefix, entityHelper);

        Assert.assertEquals("SELECT count( DISTINCT e.id ) FROM EntityClass e WHERE e.userName = ?1 ",
                new CountQueryStringBuilder(executorPrefix, entityHelper).getQuery(EntityClass.class,
                        "findDistinctByUserName"));
        EasyMock.verify(executorPrefix, entityHelper);
    }

    @Test
    public void updateCodeAndNameByUserAgeIsNotNullOrNotGreatThan() {
        executorPrefix.removePrefixifAvailable("updateCodeAndNameByUserAgeIsNotNullOrNotGreatThan");
        EasyMock.expectLastCall().andReturn("CodeAndNameByUserAgeIsNotNullOrNotGreatThan");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals(
                "UPDATE EntityClass e SET e.code = ?1 , e.name = ?2 WHERE e.user.age IS NOT NULL OR e.user.age <= ?3 ",
                new UpdateQueryStringBuilder(executorPrefix).getQuery(EntityClass.class,
                        "updateCodeAndNameByUserAgeIsNotNullOrNotGreatThan"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void deleteByName() {
        executorPrefix.removePrefixifAvailable("deleteByName");
        EasyMock.expectLastCall().andReturn("ByName");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals("DELETE FROM EntityClass e WHERE e.name = ?1 ",
                new DeleteQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "deleteByName"));
        EasyMock.verify(executorPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByUserFirstName() {
        executorPrefix.removePrefixifAvailable("getByUserFirstName");
        EasyMock.expectLastCall().andReturn("ByUserFirstName");
        EasyMock.replay(executorPrefix);

        new SelectQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "getByUserFirstName");
        EasyMock.verify(executorPrefix);
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
