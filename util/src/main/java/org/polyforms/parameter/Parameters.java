package org.polyforms.parameter;

/**
 * Aggregate class of {@link Parameter}s.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <P> type of parameter
 */
public interface Parameters<P extends Parameter> {
    /**
     * Get parameters.
     */
    P[] getParameters();
}
