package org.polyforms.event.spring;

import org.polyforms.event.NoOperation;
import org.polyforms.event.aop.NoOperationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.aop.Advisor} for methods which annotated by {@link NoOperation}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class NoOperationAdvisor extends DefaultPointcutAdvisor {
    private static final long serialVersionUID = 1L;

    /**
     * Create an default instance.
     */
    public NoOperationAdvisor() {
        super(new AnnotationMatchingPointcut(null, NoOperation.class), new NoOperationInterceptor());
    }
}
