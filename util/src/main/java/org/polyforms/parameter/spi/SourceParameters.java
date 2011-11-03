package org.polyforms.parameter.spi;

public interface SourceParameters<P extends Parameter> extends Parameters<P> {
    P getMatchedParameter(Parameter parameter);
}
