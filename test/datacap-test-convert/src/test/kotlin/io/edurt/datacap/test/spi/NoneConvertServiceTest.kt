package io.edurt.datacap.test.spi

import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Test
import kotlin.test.assertNotNull

class NoneConvertServiceTest
{
    private val pluginManager: PluginManager
    private val pluginName: String = "NoneConvert"

    init
    {
        val projectRoot = PluginPathUtils.findProjectRoot()
        val config = PluginConfigure.builder()
            .pluginsDir(projectRoot.resolve("test/datacap-test-convert"))
            .scanDepth(2)
            .build()

        pluginManager = PluginManager(config).apply { start() }
    }

    @Test
    fun test()
    {
        pluginManager.getPlugin(pluginName).ifPresent { plugin ->
            val service = plugin.getService(NoneConvertService::class.java)
            assertNotNull(service)
        }
    }
}
