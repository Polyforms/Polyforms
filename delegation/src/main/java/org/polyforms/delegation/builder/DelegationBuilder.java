package org.polyforms.delegation.builder;

/**
 * Delegation builder of class or method.
 * 
 */
public interface DelegationBuilder<T> {
    /**
     * Delegate method(s) to method(s) with same name in specific class.
     * 
     * @param delegatee the delegatee class or method
     */
    DelegationBuilder<T> to(T delegatee);

    /**
     * Delegate method to the method with same name in specific class.
     * 
     * If the method with specific parameter types can not find, the only method in specific class with method name is
     * used for delegatee.
     * 
     * @param clazz
     * @param methodName
     * @param parameterTypes
     */
    DelegationBuilder<T> to(Class<?> clazz, String methodName, final Class<?>... parameterTypes);

    /**
     * Specify the name of delegatee bean in Ioc container.
     * 
     * It only can be invoked after Method {@link #to}.
     * 
     * @param name name of bean in Ioc container
     */
    void withName(String beanName);
}
