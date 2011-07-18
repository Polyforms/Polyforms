package org.polyforms.di.container;

/**
 * Exception thrown when a bean container is asked for a bean instance which it cannot find.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@SuppressWarnings("serial")
public final class BeanNotFoundException extends RuntimeException {
    /**
     * Create an instance with cause.
     */
    public BeanNotFoundException(final Throwable cause) {
        super(cause);
    }
}
