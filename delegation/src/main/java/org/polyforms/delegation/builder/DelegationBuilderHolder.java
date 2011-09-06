package org.polyforms.delegation.builder;

/**
 * The utility class is used to hold {@link DelegationBuilder} for current thread.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class DelegationBuilderHolder {
    private static ThreadLocal<DelegationBuilder> delegationBuilder = new ThreadLocal<DelegationBuilder>();

    protected DelegationBuilderHolder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set delegation builder.
     * 
     * @param delegationBuilder
     */
    public static void set(final DelegationBuilder delegationBuilder) {
        DelegationBuilderHolder.delegationBuilder.set(delegationBuilder);
    }

    /**
     * Get delegation builder.
     * 
     * @return
     */
    public static DelegationBuilder get() {
        return delegationBuilder.get();
    }

    /**
     * Remove delegation builder.
     */
    public static void remove() {
        delegationBuilder.remove();
    }
}
