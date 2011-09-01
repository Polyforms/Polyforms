package org.polyforms.repository;

import java.util.Set;

/**
 * Helper to hold all prefix of query method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ExecutorPrefixHolder {
    /**
     * Get all aliases of specified prefix.
     * 
     * @param prefix of query method.
     * @return all aliases (include prefix) of specified prefix.
     */
    Set<String> getAliases(final String prefix);

    /**
     * Check whether specified string is a prefix.
     * 
     * @param string normally it is the name of executor method.
     * @return true if string ends with some special words.
     */
    boolean isPrefix(final String string);

    /**
     * remove prefix if specified string starts with prefix.
     * 
     * @param string normally it is the name of query method
     * @return the string after prefix if the string starts with prefix, or original string if not
     */
    String removePrefixIfAvailable(String string);
}
