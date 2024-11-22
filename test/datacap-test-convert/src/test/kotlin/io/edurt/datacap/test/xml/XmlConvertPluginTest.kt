package io.edurt.datacap.test.xml

import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import lombok.extern.slf4j.Slf4j
import org.junit.Test

@Slf4j
class XmlConvertPluginTest
{
    private val pluginManager: PluginManager
    private val pluginName: String = "XmlConvert"

    init
    {
        val projectRoot = PluginPathUtils.findProjectRoot()
        val config = PluginConfigure.builder()
            .pluginsDir(projectRoot.resolve("convert/datacap-convert-xml"))
            .scanDepth(2)
            .build()

        pluginManager = PluginManager(config).apply { start() }
    }

    @Test
    fun test()
    {
        println(pluginManager.getPlugin(pluginName))
    }
}
