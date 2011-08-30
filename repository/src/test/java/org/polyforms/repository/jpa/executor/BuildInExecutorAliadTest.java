package org.polyforms.repository.jpa.executor;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.spi.ExecutorAlias;

public class BuildInExecutorAliadTest {
    private final ExecutorAlias executorAlias = new BuildInExecutorAlias();

    @Test
    public void saveAlias() {
        final String[] alias = executorAlias.getAlias().get("save");
        Assert.assertEquals(2, alias.length);
        Assert.assertEquals("create", alias[0]);
        Assert.assertEquals("persist", alias[1]);
    }

    @Test
    public void updateAlias() {
        final String[] alias = executorAlias.getAlias().get("update");
        Assert.assertEquals(1, alias.length);
        Assert.assertEquals("merge", alias[0]);
    }

    @Test
    public void deleteAlias() {
        final String[] alias = executorAlias.getAlias().get("delete");
        Assert.assertEquals(1, alias.length);
        Assert.assertEquals("remove", alias[0]);
    }
}
