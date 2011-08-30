package org.polyforms.repository.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorAliasHolder;
import org.polyforms.repository.spi.ExecutorAlias;

public class SimpleExecutorAliasHolderTest {
    private final ExecutorAlias executorAlias = new ExecutorAlias() {
        public Map<String, String[]> getAlias() {
            final Map<String, String[]> alias = new HashMap<String, String[]>();
            alias.put("get", new String[] { "load" });
            return alias;
        }
    };
    private ExecutorAliasHolder executorAliasHolder;

    @Before
    public void setUp() {
        executorAliasHolder = new SimpleExecutorAliasHolder(Collections.singleton(executorAlias));
    }

    @Test
    public void getAlias() {
        final Set<String> alias = executorAliasHolder.getAlias("get");
        Assert.assertEquals(1, alias.size());
        Assert.assertTrue(alias.contains("load"));
    }

    @Test
    public void getNotExistAlias() {
        Assert.assertTrue(executorAliasHolder.getAlias("notExist").isEmpty());
    }
}
