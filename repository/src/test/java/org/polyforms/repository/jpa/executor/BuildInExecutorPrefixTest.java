package org.polyforms.repository.jpa.executor;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.spi.ExecutorPrefix;

public class BuildInExecutorPrefixTest {
    private final ExecutorPrefix executorPrefix = new BuildInExecutorPrefix();

    @Test
    public void savePrefix() {
        final String[] prefix = executorPrefix.getPrefix().get("save");
        Assert.assertEquals(2, prefix.length);
        Assert.assertEquals("create", prefix[0]);
        Assert.assertEquals("persist", prefix[1]);
    }

    @Test
    public void updatePrefix() {
        final String[] prefix = executorPrefix.getPrefix().get("update");
        Assert.assertEquals(1, prefix.length);
        Assert.assertEquals("merge", prefix[0]);
    }

    @Test
    public void deletePrefix() {
        final String[] prefix = executorPrefix.getPrefix().get("delete");
        Assert.assertEquals(1, prefix.length);
        Assert.assertEquals("remove", prefix[0]);
    }
}
