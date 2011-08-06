package org.polyforms.delegation.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Utilities for working with method by reflection.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class MethodUtils {
    protected MethodUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Find method from interfaces and/or classes according to name and parameter types.
     * 
     * If there is no exact method matching the name and parameter types, and parameter types is empty, the method
     * matching the name is returned.
     * 
     * The order of searching is class, super classes, interfaces in order. {@link IllegalArgumentException} is thrown
     * if there is more than one method with specific name in the interface or class.
     * 
     * @param clazz the interface or class to resolve method against
     * @param methodName the name of resolving method
     * @param parameterTypes the parameter types of resolving method
     * @return the corresponding method or null if the method cannot be found
     */
    public static Method findMostSpecificMethod(final Class<?> clazz, final String methodName,
            final Class<?>... parameterTypes) {
        validateParameters(clazz, methodName);

        Method method = ClassUtils.getMethodIfAvailable(clazz, methodName, parameterTypes);
        if (method == null && parameterTypes.length == 0) {
            method = findMethodInHierarchy(clazz, methodName);
        }
        return method;
    }

    private static void validateParameters(final Class<?> clazz, final String methodName) {
        if (clazz == null) {
            throw new IllegalArgumentException("Parameter clazz (Class<?>) must not be null.");
        }
        if (!StringUtils.hasText(methodName)) {
            throw new IllegalArgumentException("Parameter methodName (String) must not be null or empty.");
        }
    }

    private static Method findMethodInHierarchy(final Class<?> clazz, final String methodName) {
        Method method = findMethod(clazz, methodName);
        if (method != null) {
            return method;
        }

        method = findMethodInSuperClass(clazz, methodName);
        if (method != null) {
            return method;
        }

        return findMethodInInterfaces(clazz, methodName);
    }

    private static Method findMethod(final Class<?> clazz, final String methodName) {
        final List<Method> candidates = findMethods(clazz, methodName);

        if (candidates.size() > 1) {
            throw new IllegalArgumentException("Too many method' name is " + methodName
                    + ", please specify parameter types.");
        }

        return candidates.isEmpty() ? null : candidates.get(0);
    }

    private static List<Method> findMethods(final Class<?> clazz, final String methodName) {
        final List<Method> methods = new ArrayList<Method>();
        for (final Method candidate : clazz.getDeclaredMethods()) {
            if (!candidate.isBridge() && Modifier.isPublic(candidate.getModifiers())
                    && candidate.getName().equals(methodName)) {
                methods.add(candidate);
            }
        }
        return methods;
    }

    private static Method findMethodInSuperClass(final Class<?> clazz, final String methodName) {
        final Class<?> superClass = clazz.getSuperclass();
        if (superClass == null) {
            return null;
        }

        final Method method = findMethod(superClass, methodName);
        if (method != null) {
            return method;
        }

        return findMethodInSuperClass(superClass, methodName);
    }

    private static Method findMethodInInterfaces(final Class<?> clazz, final String methodName) {
        for (final Class<?> interfaze : clazz.getInterfaces()) {
            final Method method = findMethod(interfaze, methodName);
            if (method != null) {
                return method;
            }
        }

        return null;
    }
}
