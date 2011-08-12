package org.polyforms.repository.jpa.executor;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.spi.ExecutorAlias;

public class BuildInExecutorAliasTest {
    private final ExecutorAlias executorAlias = new BuildInExecutorAlias();

    @Test
    public void saveAlias() {
        final Set<String> alias = executorAlias.getAlias("save");
        Assert.assertEquals(2, alias.size());
        Assert.assertTrue(alias.contains("create"));
        Assert.assertTrue(alias.contains("persist"));
    }

    @Test
    public void removeAlias() {
        final Set<String> alias = executorAlias.getAlias("remove");
        Assert.assertEquals(1, alias.size());
        Assert.assertTrue(alias.contains("delete"));
    }

    @Test
    public void updateAlias() {
        final Set<String> alias = executorAlias.getAlias("update");
        Assert.assertEquals(2, alias.size());
        Assert.assertTrue(alias.contains("delete"));
        Assert.assertTrue(alias.contains("remove"));
    }

    @Test
    public void notExistAlias() {
        Assert.assertTrue(executorAlias.getAlias("notExist").isEmpty());
    }
}
