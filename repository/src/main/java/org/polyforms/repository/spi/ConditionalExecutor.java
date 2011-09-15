package org.polyforms.repository.spi;

import java.lang.reflect.Method;

/**
 * Interface for conditionally executing methods in Repository.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ConditionalExecutor extends Executor {
    /**
     * Should the executor be executed?
     * 
     * @param method to be executed by executor
     * 
     * @return true if executor should be executed, false otherwise
     */
    boolean matches(final Method method);
}
