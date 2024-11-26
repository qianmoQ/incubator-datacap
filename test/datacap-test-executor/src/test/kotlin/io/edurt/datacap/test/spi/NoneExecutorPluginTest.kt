package io.edurt.datacap.test.spi

import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Test
import kotlin.test.assertTrue

class NoneExecutorPluginTest
{
    private val pluginManager: PluginManager
    private val pluginName = "NoneExecutor"

    init
    {
        val projectRoot = PluginPathUtils.findProjectRoot()
        val config = PluginConfigure.builder()
            .pluginsDir(projectRoot.resolve("test/datacap-test-executor"))
            .scanDepth(2)
            .build()

        pluginManager = PluginManager(config).apply { start() }
    }

    @Test
    fun test()
    {
        assertTrue(pluginManager.getPlugin(pluginName).isPresent)
    }
}
