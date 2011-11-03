package org.polyforms.parameter.spi;

public interface Parameters<P extends Parameter> {
    P[] getParameters();
}
