package org.polyforms.repository.spi;

import java.lang.reflect.Method;

/**
 * Strategy of finding executor to execute specified method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ExecutorFinder {
    /**
     * Get matched executor to execute specific method.
     * 
     * @param method to be executed by {@link Executor}
     * 
     * @return matched executor or {@link Executor#UNSUPPORTED} if not found
     */
    Executor findExecutor(Method method);
}
