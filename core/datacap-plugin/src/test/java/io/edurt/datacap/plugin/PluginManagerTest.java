package io.edurt.datacap.plugin;

import io.edurt.datacap.plugin.utils.PluginPathUtils;
import org.junit.Test;

import java.nio.file.Path;

public class PluginManagerTest
{
    @Test
    public void testLoadPlugin()
    {
        Path projectRoot = PluginPathUtils.findProjectRoot();
        PluginConfig config = PluginConfig.builder()
                .pluginsDir(projectRoot.resolve("test/datacap-test-plugin"))
                .build();

        PluginManager pluginManager = new PluginManager(config);
        pluginManager.start();

        System.out.println(pluginManager.getPluginInfos());
    }
}
