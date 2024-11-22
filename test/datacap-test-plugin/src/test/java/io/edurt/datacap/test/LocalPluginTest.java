package io.edurt.datacap.test;

import io.edurt.datacap.plugin.PluginConfigure;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.plugin.loader.InjectPluginLoader;
import io.edurt.datacap.plugin.loader.PluginLoaderFactory;
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
        PluginConfigure config = PluginConfigure.builder()
                .pluginsDir(projectRoot.resolve("test/datacap-test-plugin"))
                .scanDepth(2)
                .build();

        PluginLoaderFactory.registerLoader(new InjectPluginLoader());
        pluginManager = new PluginManager(config);
        pluginManager.start();
    }

    @Test
    public void testLoadPlugin()
    {
        Assert.assertFalse(pluginManager.getPlugin("Local").isEmpty());
    }

    @Test
    public void testAnnotationPlugin()
    {
        pluginManager.getPlugin("Annotation")
                .ifPresent(value -> {
                    LogService logService = value.getService(LogService.class);
                    logService.print();
                });
    }
}
