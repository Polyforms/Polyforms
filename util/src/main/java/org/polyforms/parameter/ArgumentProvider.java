package org.polyforms.parameter;

import java.lang.reflect.Method;

/**
 * Provider used to resolve argument for delegatee method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ArgumentProvider {
    /**
     * Check whether the arguments can be resolved or not.
     * 
     * @param delegatee method
     */
    void validate(Method method);

    /**
     * Resolve argument from invocation arguments of method.
     * 
     * @param arguments of method
     * @return argument used by method
     */
    Object get(Object... arguments);
}
