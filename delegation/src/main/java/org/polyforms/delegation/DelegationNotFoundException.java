package org.polyforms.delegation;

import java.lang.reflect.Method;

/**
 * Exception thrown when a {@link DelegationService} is asked for a delegation which it cannot find by specific
 * {@link Method}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@SuppressWarnings("serial")
public final class DelegationNotFoundException extends RuntimeException {
    /**
     * Create an instance with source {@link Method} which cannot be delegated.
     */
    public DelegationNotFoundException(final Method method) {
        super("No delegation found capable of " + method + ".");
    }
}
