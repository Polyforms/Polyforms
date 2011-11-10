package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;

import org.polyforms.parameter.ArgumentProvider;
import org.polyforms.parameter.provider.ArgumentAt;

/**
 * Parameter Matcher used to match parameters from two methods.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class MethodParameterMatcher extends AbstractParameterMatcher<MethodParameter, MethodParameter> {
    private final ArgumentProviderBuilder argumentProviderBuilder = new ArgumentProviderBuilder();

    @Override
    protected ArgumentProvider getArgumentProvider(final SourceParameters sourceParameters,
            final MethodParameter parameter) {
        final Annotation annotation = parameter.getAnnotation();
        if (annotation != null) {
            return argumentProviderBuilder.fromAnnotation(annotation);
        }

        return new ArgumentAt(sourceParameters.match(parameter).getIndex());
    }
}
