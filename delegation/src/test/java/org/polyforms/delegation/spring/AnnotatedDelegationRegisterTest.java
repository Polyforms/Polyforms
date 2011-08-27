package org.polyforms.delegation.spring;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.DelegateTo;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.AnnotatedDelegationRegister;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.BeanClassVisitor;

public class AnnotatedDelegationRegisterTest {
    private DelegationBuilder delegationBuilder;
    private BeanClassVisitor beanClassVisitor;

    @Before
    public void setUp() {
        delegationBuilder = EasyMock.createMock(DelegationBuilder.class);
        beanClassVisitor = new AnnotatedDelegationRegister(delegationBuilder);
    }

    @Test
    public void visitNotAnnotatedClass() {
        beanClassVisitor.visit(String.class);
    }

    @Test
    public void visitAnnotatedClass() {
        delegationBuilder.delegateFrom(AnnotatedClass.class);
        EasyMock.expectLastCall().andReturn(new AnnotatedClass());
        delegationBuilder.withName("");
        delegationBuilder.delegate();
        EasyMock.expectLastCall().andReturn(null);
        delegationBuilder.registerDelegations();
        EasyMock.replay(delegationBuilder);

        beanClassVisitor.visit(AnnotatedClass.class);
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void visitAnnotatedMethod() {
        delegationBuilder.delegateFrom(AnnotatedMethod.class);
        EasyMock.expectLastCall().andReturn(new AnnotatedMethod());
        delegationBuilder.delegateTo(AnnotatedClass.class);
        EasyMock.expectLastCall().times(2);
        delegationBuilder.withName("");
        EasyMock.expectLastCall().times(2);
        delegationBuilder.delegate();
        EasyMock.expectLastCall().andReturn(new AnnotatedClass()).times(2);
        delegationBuilder.registerDelegations();
        EasyMock.replay(delegationBuilder);

        beanClassVisitor.visit(AnnotatedMethod.class);
        EasyMock.verify(delegationBuilder);
    }

    @DelegateTo
    static class AnnotatedClass {
        public void mockMethod() {
        }
    }

    static class AnnotatedMethod {
        @DelegateTo(AnnotatedClass.class)
        public void mockMethod() {
        }

        @DelegateTo(value = AnnotatedClass.class, methodName = "mockMethod")
        public void mockMethod2() {
        }
    }
}
