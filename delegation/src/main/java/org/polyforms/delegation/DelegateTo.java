package org.polyforms.delegation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that method are delegated to another method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DelegateTo {
    /**
     * The type of bean which delegatee method in.
     * 
     * Default value is Void.class which means delegating to first parameter.
     */
    Class<?> value() default Void.class;

    /**
     * The name of method which is delegated.
     * 
     * Default value is empty string which means same name with annotated method.
     */
    String methodName() default "";

    /**
     * The parameter types of delegatee method, which is used when method overload exists.
     * 
     * The specific parameter types are using to find the exactly matching method. If not found, the only method with
     * specific name in {link #methodName()) is an alternative.
     */
    Class<?>[] parameterTypes() default {};

    /**
     * The name of bean which delegatee method in.
     * 
     * Default value is empty string which means using {@link #value()} to determine bean object in Ioc container.
     * Otherwise, the bean in Ioc container with specific name and type is used.
     */
    String name() default "";
}
