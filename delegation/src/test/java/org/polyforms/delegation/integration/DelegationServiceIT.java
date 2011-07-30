package org.polyforms.delegation.integration;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.polyforms.delegation.Delegate;
import org.polyforms.delegation.DelegationRegister;
import org.polyforms.delegation.DelegationService;
import org.polyforms.delegation.builder.DelegationBuilderFactory;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("ComponentScannerIT-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DelegationServiceIT {
    @Autowired
    private DelegationService delegationService;
    @Autowired
    private Delegator delegator;
    @Autowired
    private AbstractDelegator abstractDelegator;
    @Autowired
    private DelegationRegistry delegationRegistry;
    @Autowired
    private AnnotationDelegator annotationDelegator;
    private DelegationBuilderFactory delegationBuilder;

    @Before
    public void setUp() {
        delegationBuilder = new DelegationBuilderFactory(delegationRegistry);
    }

    @Test
    public void delegateAbstractClass() {
        Assert.assertEquals("1", abstractDelegator.echo("1"));
    }

    @Test
    public void beanDelegation() {
        Assert.assertEquals("1", delegator.echo("1"));
    }

    @Test
    public void domainDelegation() {
        Assert.assertEquals(4, delegator.length("test"));
    }

    @Test
    public void delegationWithMoreParameters() {
        Assert.assertEquals("CN", delegator.getCountry("zh_CN", 1));
    }

    @Test
    public void delegateToVoidMethod() {
        delegator.voidMethod("Test");
    }

    @Test
    public void delegateByVoidMethod() {
        Assert.assertEquals(0, delegator.length());
    }

    @Test
    public void delegateTo() {
        Assert.assertEquals(4, annotationDelegator.getLength("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void delegationWithLessParameters() {
        delegator.hello();
    }

    @Test(expected = IllegalArgumentException.class)
    public void domainDelegationWithNullArgument() {
        delegator.length(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void domainDelegationWithoutParameters() {
        delegator.name();
    }

    @Test
    public void cannotDelegateNullMethod() {
        Assert.assertFalse(delegationService.canDelegate(null));
    }

    @Test(expected = DelegateException.class)
    public void testException() {
        delegator.exception();
    }

    @Test(expected = DelegateException.class)
    public void testExceptionWithName() {
        delegator.exceptionWithName(true);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionWithoutException() {
        delegator.exceptionWithName(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerInexistentDelegator() {
        new DelegationRegister() {
            public void registerDelegations(final DelegationBuilderFactory delegationBuilder) {
                delegationBuilder.delegate(Delegator.class, "inexistentMethod");
            }
        }.registerDelegations(delegationBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerInexistentDelegatee() {
        new DelegationRegister() {
            public void registerDelegations(final DelegationBuilderFactory delegationBuilder) {
                delegationBuilder.delegate(Delegator.class, "length").to(String.class, "inexistentMethod");
            }
        }.registerDelegations(delegationBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerClassDelegator() {
        new DelegationRegister() {
            public void registerDelegations(final DelegationBuilderFactory delegationBuilder) {
                delegationBuilder.delegate(Delegatee.class);
            }
        }.registerDelegations(delegationBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNameBeforeTo() {
        new DelegationRegister() {
            public void registerDelegations(final DelegationBuilderFactory delegationBuilder) {
                delegationBuilder.delegate(Delegator.class).withName("bean");
            }
        }.registerDelegations(delegationBuilder);
    }

    @Component
    public static class TestDelegationBuilder implements DelegationRegister {
        public void registerDelegations(final DelegationBuilderFactory delegationBuilder) {
            delegationBuilder.delegate(AbstractDelegator.class).to(Delegatee.class);
            delegationBuilder.delegate(Delegator.class, "length", String.class).to(String.class, "length");
            delegationBuilder.delegate(Delegator.class).to(Delegatee.class).withName("delegationServiceIT.Delegatee");
            delegationBuilder.delegate(Delegator.class, "name").to(String.class, "toString");
            delegationBuilder.delegate(Delegator.class, "name").to(String.class, "length");
            delegationBuilder.delegate(Delegator.class, "getCountry").to(Locale.class, "getCountry");
            delegationBuilder.delegate(Delegator.class, "length").to(Delegatee.class, "voidMethod");
            delegationBuilder.delegate(Delegator.class, "voidMethod").to(String.class, "length");
        }
    }

    @Component
    public static interface Delegator {
        String echo(String string);

        int length(String string);

        String name();

        String getCountry(String locale, int start);

        String hello();

        void voidMethod(String string);

        int length();

        void exception() throws IllegalArgumentException, DelegateException;

        void exceptionWithName(boolean exception) throws DelegateException;
    }

    @Component
    public static abstract interface AbstractDelegator extends AbstractInterface<String> {
    }

    public static interface AbstractInterface<T> {
        T echo(T String);
    }

    @Component
    public static interface AnnotationDelegator {
        String echo(String string);

        @Delegate(value = String.class, methodName = "length")
        int getLength(String string);
    }

    public static interface GenericDelegatee<T extends Number> {
        T echo(T object);
    }

    public static class GenericDelegateeImpl<T extends Number> implements GenericDelegatee<T> {
        public T echo(final T object) {
            if (object instanceof Integer) {
                return object;
            }
            return null;
        }
    }

    @Component
    public static class Delegatee extends GenericDelegateeImpl<Integer> {
        public String hello(final String name) {
            return "hello " + name;
        }

        public void voidMethod() {
        }

        public void exception() throws IllegalArgumentException, MockException {
            throw new MockException();
        }

        public void exceptionWithName(final boolean exception) {
            if (exception) {
                throw new DelegateException();
            } else {
                throw new RuntimeException();
            }
        }

        @SuppressWarnings("serial")
        private class MockException extends RuntimeException {
        }

        @SuppressWarnings("serial")
        private static class DelegateException extends RuntimeException {
        }
    }

    @SuppressWarnings("serial")
    public static class DelegateException extends RuntimeException {
    }
}

@Configuration
class Config {
    @Bean
    public Object configuredObject() {
        return new Object();
    }
}
