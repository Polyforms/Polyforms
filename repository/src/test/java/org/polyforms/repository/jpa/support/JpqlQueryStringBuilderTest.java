package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

public class JpqlQueryStringBuilderTest {
    private final QueryResolver queryResolver = new JpqlQueryStringBuilder();

    @Test
    public void byName() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.name = ?1 ",
                queryResolver.getQuery(Object.class, getMethod("byName")));
        // Just for testing cache
        Assert.assertEquals("SELECT e FROM String e WHERE e.name = ?1 ",
                queryResolver.getQuery(String.class, getMethod("byName")));
    }

    @Test
    public void orderById() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e ORDER BY e.id ",
                queryResolver.getQuery(Object.class, getMethod("orderById")));
    }

    @Test
    public void getByNameBetween() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.name BETWEEN ?1 AND ?2 ",
                queryResolver.getQuery(Object.class, getMethod("getByNameBetween")));
    }

    @Test
    public void getDistinctByFirstNameAndCodeIn() throws NoSuchMethodException {
        Assert.assertEquals("SELECT DISTINCT e FROM Object e WHERE e.firstName = ?1 AND e.code IN ?2 ",
                queryResolver.getQuery(Object.class, getMethod("getDistinctByFirstNameAndCodeIn")));
    }

    @Test
    public void getByNameOrderByIdDesc() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.name = ?1 ORDER BY e.id DESC ",
                queryResolver.getQuery(Object.class, getMethod("getByNameOrderByIdDesc")));
    }

    @Test
    public void findByAgeIsNotNullOrNotGreatThan() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.age IS NOT NULL OR e.age <= ?1 ",
                queryResolver.getQuery(Object.class, getMethod("findByAgeIsNotNullOrNotGreatThan")));
    }

    private Method getMethod(final String methodName) throws NoSuchMethodException {
        return MockInterface.class.getMethod(methodName, new Class<?>[0]);
    }

    interface MockInterface {
        void byName();

        void orderById();

        void getByNameBetween();

        void getDistinctByFirstNameAndCodeIn();

        void getByNameOrderByIdDesc();

        void findByAgeIsNotNullOrNotGreatThan();
    }
}
