package org.polyforms.delegation.builder;

/**
 * Exception thrown when a {@link DelegationRegistry.Delegation} is asked for a specific method which it cannot find in
 * {@link DelegationRegistry}
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@SuppressWarnings("serial")
public final class DelegationRegistrationException extends RuntimeException {
    /**
     * Create an instance with root cause.
     */
    public DelegationRegistrationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create an instance with error message.
     */
    public DelegationRegistrationException(final String message) {
        super(message);
    }
}
