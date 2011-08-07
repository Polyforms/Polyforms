package org.polyforms.delegation.builder.support;

interface ProxyFactory {
    <T> T getProxy(Class<T> proxyClass);
}
