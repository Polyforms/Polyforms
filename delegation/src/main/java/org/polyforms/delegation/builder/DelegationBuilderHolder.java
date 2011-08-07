package org.polyforms.delegation.builder;

public final class DelegationBuilderHolder {
    private static ThreadLocal<DelegationBuilder> delegationBuilder = new ThreadLocal<DelegationBuilder>();

    protected DelegationBuilderHolder() {
        throw new UnsupportedOperationException();
    }

    public static void set(final DelegationBuilder delegationBuilder) {
        DelegationBuilderHolder.delegationBuilder.set(delegationBuilder);
    }

    public static DelegationBuilder get() {
        return delegationBuilder.get();
    }

    public static void remove() {
        delegationBuilder.remove();
    }
}
