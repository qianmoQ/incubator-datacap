package io.edurt.datacap.plugin;

import io.edurt.datacap.plugin.utils.PluginPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

@Slf4j
public class PluginManagerTest
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
        pluginManager.getPluginInfos().forEach(info -> log.info("{}", info));

        Assert.assertFalse(pluginManager.getPlugin("Local").isEmpty());
    }
}
