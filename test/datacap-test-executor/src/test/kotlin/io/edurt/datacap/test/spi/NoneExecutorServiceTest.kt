package io.edurt.datacap.test.spi

import io.edurt.datacap.executor.configure.ExecutorConfigure
import io.edurt.datacap.executor.configure.ExecutorRequest
import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Assert.assertNotNull
import org.junit.Test

class NoneExecutorServiceTest
{
    private val pluginManager: PluginManager
    private val pluginName: String = "NoneExecutor"

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
        pluginManager.getPlugin(pluginName).ifPresent { plugin ->
            val service = plugin.getService(NoneExecutorService::class.java)
            service.start(ExecutorRequest(null, ExecutorConfigure(null), ExecutorConfigure(null)))
            assertNotNull(service)
        }
    }
}
