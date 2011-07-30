package org.polyforms.delegation.spring;

import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Test;
import org.polyforms.delegation.Delegate;
import org.polyforms.delegation.builder.DelegationBuilderFactory;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.AnnotatedDelegationRegister;

public class AnnotatedDelegationRegisterTest {
    @Test
    public void visit() throws NoSuchMethodException {
        final DelegationRegistry delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
        Method getLength = AnnotationDelegator.class.getMethod("getLength", new Class<?>[] { String.class });
        delegationRegistry.contains(getLength);
        EasyMock.expectLastCall().andReturn(false);
        delegationRegistry.register(new Delegation(getLength, String.class.getMethod("length", new Class<?>[0])));
        Method length = AnnotationDelegator.class.getMethod("length", new Class<?>[] { String.class });
        delegationRegistry.contains(length);
        EasyMock.expectLastCall().andReturn(false);
        delegationRegistry.register(new Delegation(length, String.class.getMethod("length", new Class<?>[0])));
        EasyMock.replay(delegationRegistry);

        final AnnotatedDelegationRegister annotatedDelegationRegister = new AnnotatedDelegationRegister(
                new DelegationBuilderFactory(delegationRegistry));
        annotatedDelegationRegister.visit(null, null, AnnotationDelegator.class);
        EasyMock.verify(delegationRegistry);
    }

    public static interface AnnotationDelegator {
        String echo(String string);

        @Delegate(value = String.class, methodName = "length")
        int getLength(String string);

        @Delegate(value = String.class)
        int length(String string);
    }
}
