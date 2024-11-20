package io.edurt.datacap.test;

import io.edurt.datacap.plugin.PluginConfig;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.plugin.utils.PluginPathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

public class LocalPluginTest
{
    private PluginManager pluginManager;

    @Before
    public void before()
    {
        Path projectRoot = PluginPathUtils.findProjectRoot();
        PluginConfig config = PluginConfig.builder()
                .pluginsDir(projectRoot.resolve("test/datacap-test-plugin"))
                .build();

        pluginManager = new PluginManager(config);
        pluginManager.start();
    }

    @Test
    public void testLoadPlugin()
    {
        Assert.assertFalse(pluginManager.getPlugin("Local").isEmpty());
    }
}
