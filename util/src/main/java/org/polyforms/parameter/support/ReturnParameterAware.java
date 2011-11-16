package org.polyforms.parameter.support;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;

public interface ReturnParameterAware<P extends Parameter> extends Parameters<P> {
    /**
     * Get parameter of return value.
     * 
     * @return null if return type is void.
     */
    P getReturnParameter();
}
