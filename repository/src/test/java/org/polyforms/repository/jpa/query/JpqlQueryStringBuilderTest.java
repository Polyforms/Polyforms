package org.polyforms.repository.jpa.query;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefixHolder;

public class JpqlQueryStringBuilderTest {
    private ExecutorPrefixHolder executorPrefix;

    @Before
    public void setUp() {
        executorPrefix = EasyMock.createMock(ExecutorPrefixHolder.class);
    }

    @Test
    public void byName() {
        executorPrefix.removePrefixIfAvailable("byName");
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
        executorPrefix.removePrefixIfAvailable("getDistinctByNameAndCodeInOrderByIdDesc");
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
        executorPrefix.removePrefixIfAvailable("orderById");
        EasyMock.expectLastCall().andReturn("orderById");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals("SELECT e FROM EntityClass e ORDER BY e.id ",
                new SelectQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "orderById"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void countByUser_NameBetween() {
        executorPrefix.removePrefixIfAvailable("findByUser_NameBetween");
        EasyMock.expectLastCall().andReturn("ByUser_NameBetween");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals("SELECT count( e ) FROM EntityClass e WHERE e.user.name BETWEEN ?1 AND ?2 ",
                new CountQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "findByUser_NameBetween"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void countByUserNameOrderByIdDesc() {
        executorPrefix.removePrefixIfAvailable("findDistinctByUserName");
        EasyMock.expectLastCall().andReturn("findDistinctByUserName");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals("SELECT count( DISTINCT e ) FROM EntityClass e WHERE e.userName = ?1 ",
                new CountQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "findDistinctByUserName"));
        EasyMock.verify(executorPrefix);
    }

    @Test
    public void updateCodeAndNameByUserAgeIsNotNullOrNotGreatThan() {
        executorPrefix.removePrefixIfAvailable("updateCodeAndNameByUserAgeIsNotNullOrNotGreatThan");
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
        executorPrefix.removePrefixIfAvailable("deleteByName");
        EasyMock.expectLastCall().andReturn("ByName");
        EasyMock.replay(executorPrefix);

        Assert.assertEquals("DELETE FROM EntityClass e WHERE e.name = ?1 ",
                new DeleteQueryStringBuilder(executorPrefix).getQuery(EntityClass.class, "deleteByName"));
        EasyMock.verify(executorPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByUserFirstName() {
        executorPrefix.removePrefixIfAvailable("getByUserFirstName");
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
