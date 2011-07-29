package org.polyforms.delegation.spring;

import org.easymock.EasyMock;
import org.junit.Test;
import org.polyforms.delegation.Delegate;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.AnnotatedDelegationRegister;

public class AnnotatedDelegationRegisterTest {
    @Test
    public void visit() throws NoSuchMethodException {
        final DelegationRegistry delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
        delegationRegistry.register(new Delegation(AnnotationDelegator.class.getMethod("getLength",
                new Class<?>[] { String.class }), String.class.getMethod("length", new Class<?>[0])));
        delegationRegistry.register(new Delegation(AnnotationDelegator.class.getMethod("length",
                new Class<?>[] { String.class }), String.class.getMethod("length", new Class<?>[0])));
        EasyMock.replay(delegationRegistry);

        final AnnotatedDelegationRegister annotatedDelegationRegister = new AnnotatedDelegationRegister(
                new DelegationBuilder(delegationRegistry));
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
