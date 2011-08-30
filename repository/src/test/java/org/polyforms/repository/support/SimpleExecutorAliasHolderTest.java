package org.polyforms.repository.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.ExecutorPrefix;

public class SimpleExecutorAliasHolderTest {
    private final ExecutorPrefix executorPrefix = new ExecutorPrefix() {
        public Map<String, String[]> getPrefix() {
            final Map<String, String[]> prefix = new HashMap<String, String[]>();
            prefix.put("get", new String[] { "load" });
            return prefix;
        }
    };
    private ExecutorPrefixHolder executorPrefixHolder;

    @Before
    public void setUp() {
        executorPrefixHolder = new SimpleExecutorPrefixHolder(Collections.singleton(executorPrefix));
    }

    @Test
    public void getPrefix() {
        final Set<String> prefix = executorPrefixHolder.getPrefix("get");
        Assert.assertEquals(2, prefix.size());
        Assert.assertTrue(prefix.contains("get"));
        Assert.assertTrue(prefix.contains("load"));
    }

    @Test
    public void getNotExistPrefix() {
        final Set<String> prefix = executorPrefixHolder.getPrefix("notExist");
        Assert.assertEquals(1, prefix.size());
        Assert.assertTrue(prefix.contains("notExist"));
    }
}
