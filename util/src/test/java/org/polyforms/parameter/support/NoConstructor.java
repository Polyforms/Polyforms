package org.polyforms.parameter.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.polyforms.parameter.annotation.Provider;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Provider(NoConstructorProvider.class)
public @interface NoConstructor {
    int value();
}
