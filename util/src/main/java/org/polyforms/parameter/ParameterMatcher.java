package org.polyforms.parameter;

/**
 * Interface used to match parameters.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <S> type of source parameter
 * @param <T> type of target parameter
 */
public interface ParameterMatcher<S extends Parameter, T extends Parameter> {
    /**
     * Match parameters from source to target.
     * 
     * @return the argument providers of matched parameters
     * @throws IllegalArgumentException if there is parameters cannot be matched
     */
    ArgumentProvider[] match(final Parameters<S> sourceParameters, Parameters<T> targetParameters);
}
