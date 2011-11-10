package org.polyforms.delegation.builder;

import java.lang.reflect.Method;
import java.util.List;

import org.polyforms.parameter.ArgumentProvider;

/**
 * The interface for getting properties of delegation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface Delegation {
    /**
     * Get delegator type.
     * 
     * @return type of delegator
     */
    Class<?> getDelegatorType();

    /**
     * Get delegator method.
     * 
     * @return method of delegator
     */
    Method getDelegatorMethod();

    /**
     * Get delegatee type.
     * 
     * @return type of delegatee
     */
    Class<?> getDelegateeType();

    /**
     * Get delegatee method.
     * 
     * @return method of delegatee
     */
    Method getDelegateeMethod();

    /**
     * Get delegatee name.
     * 
     * @return name of delegatee
     */
    String getDelegateeName();

    /**
     * Get parameter providers.
     * 
     * @return parameter providers
     */
    List<ArgumentProvider> getArgumentProviders();

    /**
     * Get mapped exception type.
     * 
     * @param exceptionType original exception type
     * @return mapped exception type
     */
    Class<? extends Throwable> getExceptionType(Class<? extends Throwable> exceptionType);
}
