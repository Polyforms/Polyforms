package org.polyforms.delegation.support;

import junit.framework.Assert;

import org.junit.Test;

public class DelegatorTest {
    private final Delegator delegatorA;
    private final Delegator delegatorB;

    public DelegatorTest() throws NoSuchMethodException {
        delegatorA = new Delegator(String.class, String.class.getMethod("toString", new Class<?>[0]));
        delegatorB = new Delegator(String.class, String.class.getMethod("hashCode", new Class<?>[0]));
    }

    @Test
    public void getType() throws NoSuchMethodException {
        Assert.assertSame(String.class, delegatorA.getType());
    }

    @Test
    public void getMethod() throws NoSuchMethodException {
        Assert.assertEquals(String.class.getMethod("toString", new Class<?>[0]), delegatorA.getMethod());
    }

    @Test
    public void hashcode() {
        Assert.assertTrue(delegatorA.hashCode() != delegatorB.hashCode());
    }

    @Test
    public void equalsSame() {
        Assert.assertTrue(delegatorA.equals(delegatorA));
    }

    @Test
    public void notEqualsNull() {
        Assert.assertFalse(delegatorA.equals(null));
    }

    @Test
    public void notEqualsOtherClass() {
        Assert.assertFalse(delegatorA.equals(new Object()));
    }

    @Test
    public void notEqualsType() throws NoSuchMethodException {
        Assert.assertFalse(delegatorA.equals(new Delegator(Object.class, Object.class.getMethod("toString",
                new Class<?>[0]))));
    }

    @Test
    public void notEqualsMethod() {
        Assert.assertFalse(delegatorA.equals(delegatorB));
    }

    @Test
    public void equals() throws NoSuchMethodException {
        Assert.assertTrue(delegatorA.equals(new Delegator(String.class, String.class.getMethod("toString",
                new Class<?>[0]))));
    }
}
