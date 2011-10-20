package org.polyforms.delegation;

import java.lang.reflect.Method;

/**
 * The service interface for delegation. This is the entry point to the delegation system. Call
 * {@link #delegate(Class, Method, Object[])} to invoke a related method which is linked by builder.
 * 
 * The real method executed by invocation of the delegator is a method in a bean or in first parameter, which is binded
 * by delegation builder.
 * 
 * The {@link IllegalArgumentException} should be thrown if there is no bean with specified name in Ioc container or no
 * parameters while using first parameter as delegated target.
 * 
 * Parameters including all from client's invocation or might excepting first parameter while using it as delegated target,
 * would be passed to real method invocation in order.
 * 
 * if quantity of passed parameters are more than required, the redundant parameters should be ignored silently. In opposition, 
 * Exception {@link IllegalArgumentException} should be thrown if quantity of parameters are less than required.
 * 
 * Conversion might happen if type of passed and required are unmatched. Please check conversion service for more
 * detail.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationService {
    /**
     * Check if a method can be delegated to another method.
     * 
     * @param delegatorType the delegated class
     * @param delegatorMethod the delegated method
     * @return true if the delegation can be performed, false if not
     */
    boolean supports(Class<?> delegatorType, Method delegatorMethod);

    /**
     * Delegate a invocation of method. Conversion might exist if parameters and/or return value don't match between
     * delegator and delegatee.
     * 
     * @param delegatorType the delegated class
     * @param delegatorMethod the delegated method
     * @param arguments arguments for delegation
     * @return the return value of execution of delegation
     * 
     * @throws Throwable if invocation of delegatee method throw an exception
     */
    Object delegate(Class<?> delegatorType, Method delegatorMethod, Object... arguments) throws Throwable;
}
