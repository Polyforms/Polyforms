package org.polyforms.delegation.spring;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.Delegate;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.spring.DelegationRegisterProcessor.AnnotatedDelegationRegister;
import org.polyforms.di.spring.util.BeanFactoryVisitor.BeanClassVisitor;

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
        beanClassVisitor.visit(null, null, String.class);
    }

    @Test
    public void visitAnnotatedClass() {
        delegationBuilder.from(AnnotatedClass.class);
        EasyMock.expectLastCall().andReturn(new AnnotatedClass());
        delegationBuilder.delegate();
        EasyMock.expectLastCall().andReturn(null);
        delegationBuilder.registerDelegations();
        EasyMock.replay(delegationBuilder);

        beanClassVisitor.visit(null, null, AnnotatedClass.class);
        EasyMock.verify(delegationBuilder);
    }

    @Test
    public void visitAnnotatedMethod() {
        delegationBuilder.from(AnnotatedMethod.class);
        EasyMock.expectLastCall().andReturn(new AnnotatedMethod());
        delegationBuilder.delegate();
        EasyMock.expectLastCall().andReturn(null);
        delegationBuilder.registerDelegations();
        EasyMock.replay(delegationBuilder);

        beanClassVisitor.visit(null, null, AnnotatedMethod.class);
        EasyMock.verify(delegationBuilder);
    }

    @Delegate
    static class AnnotatedClass {
    }

    static class AnnotatedMethod {
        @Delegate
        public void mockMethod() {
        }
    }
}
