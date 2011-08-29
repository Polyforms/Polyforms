package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

public class JpqlQueryStringBuilderTest {
    private final QueryResolver queryResolver = new JpqlQueryStringBuilder();

    @Test
    public void byName() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.name = ?1 ",
                queryResolver.getQuery(EntityClass.class, getMethod("byName")));
        // Just for testing cache
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.name = ?1 ",
                queryResolver.getQuery(EntityClass.class, getMethod("byName")));
    }

    @Test
    public void orderById() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM EntityClass e ORDER BY e.id ",
                queryResolver.getQuery(EntityClass.class, getMethod("orderById")));
    }

    @Test
    public void getByUser_NameBetween() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.user.name BETWEEN ?1 AND ?2 ",
                queryResolver.getQuery(EntityClass.class, getMethod("getByUser_NameBetween")));
    }

    @Test
    public void getDistinctByNameAndCodeIn() throws NoSuchMethodException {
        Assert.assertEquals("SELECT DISTINCT e FROM EntityClass e WHERE e.name = ?1 AND e.code IN ?2 ",
                queryResolver.getQuery(EntityClass.class, getMethod("getDistinctByNameAndCodeIn")));
    }

    @Test
    public void getByUserNameOrderByIdDesc() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.userName = ?1 ORDER BY e.id DESC ",
                queryResolver.getQuery(EntityClass.class, getMethod("getByUserNameOrderByIdDesc")));
    }

    @Test
    public void findByUserAgeIsNotNullOrNotGreatThan() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM EntityClass e WHERE e.user.age IS NOT NULL OR e.user.age <= ?1 ",
                queryResolver.getQuery(EntityClass.class, getMethod("findByUserAgeIsNotNullOrNotGreatThan")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByUserFirstName() throws NoSuchMethodException {
        queryResolver.getQuery(EntityClass.class, getMethod("getByUserFirstName"));
    }

    private Method getMethod(final String methodName) throws NoSuchMethodException {
        return MockInterface.class.getMethod(methodName, new Class<?>[0]);
    }

    interface MockInterface {
        void byName();

        void orderById();

        void getByUser_NameBetween();

        void getDistinctByNameAndCodeIn();

        void getByUserNameOrderByIdDesc();

        void findByUserAgeIsNotNullOrNotGreatThan();

        void getByUserFirstName();
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
