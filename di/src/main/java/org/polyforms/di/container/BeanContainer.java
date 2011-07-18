package org.polyforms.di.container;

import java.util.Collection;

/**
 * The interface for accessing beans in a Ioc container.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface BeanContainer {
    /**
     * Check whether an instance of specific type exists in Ioc container.
     * 
     * @param type of the bean to match; can be an interface or superclass. {@literal null} is disallowed.
     * @return true if there is an instance of type in Ioc container, false if not
     */
    boolean containsBean(Class<?> type);

    /**
     * Return the bean instance that uniquely matches the given object type, if any.
     * 
     * @param type of the bean to match; can be an interface or superclass. {@literal null} is disallowed.
     * @return bean matching required type
     * @throws BeanNotFoundException if specific bean cannot be found
     */
    <T> T getBean(Class<T> type);

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     * <p>
     * Behaves the same as {@link #getBean(Class)}, but provides a measure of type safety by throwing a
     * BeanNotOfRequiredTypeException if the bean is not of the required type. This means that ClassCastException can't
     * be thrown on casting the result correctly, as can happen with {@link #getBean(Class)}.
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will ask the parent factory if the bean cannot
     * be found in this factory instance.
     * 
     * @param name the name of the bean to retrieve
     * @param type of the bean to match. Can be an interface or superclass of the actual class, or <code>null</code> for
     *            any match. For example, if the value is <code>Object.class</code>, this method will succeed whatever
     *            the class of the returned instance.
     * @return an instance of the bean
     * @throws BeanNotFoundException if specific bean cannot be found
     */
    <T> T getBean(String name, Class<T> type);

    /**
     * Return the bean instances that match the given object type (including subclasses).
     * 
     * @param type of the class or interface to match, or <code>null</code> for all concrete beans
     * @return a Collection with the matching beans
     */
    <T> Collection<T> getBeans(Class<T> type);
}
