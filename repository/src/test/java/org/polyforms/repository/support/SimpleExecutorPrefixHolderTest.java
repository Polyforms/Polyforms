package org.polyforms.repository.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefixHolder;
import org.polyforms.repository.spi.ExecutorPrefixAlias;

public class SimpleExecutorPrefixHolderTest {
    private ExecutorPrefixHolder executorPrefix;

    @Before
    public void setUp() {
        final ExecutorPrefixAlias executorAlias1 = new ExecutorPrefixAlias() {
            public Map<String, String[]> getAliases() {
                final Map<String, String[]> prefix = new HashMap<String, String[]>();
                prefix.put("get", new String[] { "load" });
                return prefix;
            }
        };
        final ExecutorPrefixAlias executorAlias2 = new ExecutorPrefixAlias() {
            public Map<String, String[]> getAliases() {
                final Map<String, String[]> prefix = new HashMap<String, String[]>();
                prefix.put("get", new String[] { "find" });
                return prefix;
            }
        };
        final Set<ExecutorPrefixAlias> alias = new HashSet<ExecutorPrefixAlias>();
        alias.add(executorAlias1);
        alias.add(executorAlias2);
        executorPrefix = new SimpleExecutorPrefixHolder(alias);
    }

    @Test
    public void getPrefix() {
        final Set<String> prefix = executorPrefix.getAliases("get");
        Assert.assertEquals(3, prefix.size());
        Assert.assertTrue(prefix.contains("get"));
        Assert.assertTrue(prefix.contains("load"));
        Assert.assertTrue(prefix.contains("find"));
    }

    @Test
    public void removePrefix() {
        Assert.assertEquals("ByName", executorPrefix.removePrefixIfAvailable("getByName"));
    }

    @Test
    public void notRemovePrefix() {
        Assert.assertEquals("ByName", executorPrefix.removePrefixIfAvailable("ByName"));
    }

    @Test
    public void getNotExistPrefix() {
        final Set<String> prefix = executorPrefix.getAliases("findBy");
        Assert.assertEquals(1, prefix.size());
        Assert.assertTrue(prefix.contains("find"));
    }

    @Test
    public void emptyExecutorPrefixAlias() {
        Assert.assertTrue(new EmptyExecutorPrefixAlias().getAliases().isEmpty());
    }
}
