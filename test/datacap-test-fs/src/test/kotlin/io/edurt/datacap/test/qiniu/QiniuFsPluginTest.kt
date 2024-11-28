package io.edurt.datacap.test.qiniu

import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Test
import kotlin.test.assertNotNull

class QiniuFsPluginTest
{
    private val pluginManager: PluginManager
    private val pluginName = "QiniuFs"

    init
    {
        val projectRoot = PluginPathUtils.findProjectRoot()
        val config = PluginConfigure.builder()
            .pluginsDir(projectRoot.resolve("fs/datacap-fs-qiniu"))
            .build()

        pluginManager = PluginManager(config).apply { start() }
    }

    @Test
    fun test()
    {
        val plugin = pluginManager.getPlugin(pluginName)
        assertNotNull(plugin.get())
    }
}
