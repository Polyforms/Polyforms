package org.polyforms.repository.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.spi.ExecutorPrefixAlias;

public class SimpleExecutorPrefixTest {
    private final ExecutorPrefixAlias executorAlias = new ExecutorPrefixAlias() {
        public Map<String, String[]> getAlias() {
            final Map<String, String[]> prefix = new HashMap<String, String[]>();
            prefix.put("get", new String[] { "load" });
            return prefix;
        }
    };
    private ExecutorPrefix executorPrefix;

    @Before
    public void setUp() {
        executorPrefix = new SimpleExecutorPrefix(Collections.singleton(executorAlias));
    }

    @Test
    public void getPrefix() {
        final Set<String> prefix = executorPrefix.getPrefix("get");
        Assert.assertEquals(2, prefix.size());
        Assert.assertTrue(prefix.contains("get"));
        Assert.assertTrue(prefix.contains("load"));
    }

    @Test
    public void removePrefix() {
        Assert.assertEquals("ByName", executorPrefix.removePrefixifAvailable("getByName"));
    }

    @Test
    public void notRemovePrefix() {
        Assert.assertEquals("ByName", executorPrefix.removePrefixifAvailable("ByName"));
    }

    @Test
    public void getNotExistPrefix() {
        final Set<String> prefix = executorPrefix.getPrefix("findBy");
        Assert.assertEquals(1, prefix.size());
        Assert.assertTrue(prefix.contains("find"));
    }

    @Test
    public void emptyExecutorPrefixAlias() {
        Assert.assertTrue(new EmptyExecutorPrefixAlias().getAlias().isEmpty());
    }
}
