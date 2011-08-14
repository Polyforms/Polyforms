package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class MethodBasedQueryStringBuilderTest {
    private QueryResolver queryResolver;

    @Before
    public void setUp() {
        queryResolver = new MethodBasedQueryStringBuilder();
    }

    @Test
    public void getByName() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.name = ?1 ",
                queryResolver.getQuery(Object.class, getMethod("getByName")));
    }

    @Test
    public void getByNameBetween() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.name BETWEEN ?1 AND ?2 ",
                queryResolver.getQuery(Object.class, getMethod("getByNameBetween")));
    }

    @Test
    public void getDistinctByNameAndCode() throws NoSuchMethodException {
        Assert.assertEquals("SELECT DISTINCT e FROM Object e WHERE e.name = ?1 AND e.code = ?2 ",
                queryResolver.getQuery(Object.class, getMethod("getDistinctByNameAndCode")));
    }

    @Test
    public void getByNameOrderById() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e WHERE e.name = ?1 ORDER BY e.id ",
                queryResolver.getQuery(Object.class, getMethod("getByNameOrderById")));
    }

    @Test
    public void getOrderById() throws NoSuchMethodException {
        Assert.assertEquals("SELECT e FROM Object e ORDER BY e.id ",
                queryResolver.getQuery(Object.class, getMethod("getOrderById")));
    }

    private Method getMethod(final String methodName) throws NoSuchMethodException {
        return MockInterface.class.getMethod(methodName, new Class<?>[0]);
    }

    interface MockInterface {
        void getByName();

        void getByNameBetween();

        void getDistinctByNameAndCode();

        void getByNameOrderById();

        void getOrderById();
    }
}
