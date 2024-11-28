package io.edurt.datacap.test

import io.edurt.datacap.fs.FsRequest
import io.edurt.datacap.fs.FsService
import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Assert.assertNotNull
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*
import kotlin.test.assertTrue

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
abstract class BaseServiceTest(
    private val pluginName: String,
    pluginHome: String,
    pluginPrefix: String
)
{
    private val log = LoggerFactory.getLogger(this::class.java)
    private val pluginManager: PluginManager
    private val fileName = "${pluginName}PluginTest.kt"
    private val request: FsRequest

    init
    {
        val projectRoot = PluginPathUtils.findProjectRoot()
        val config = PluginConfigure.builder()
            .pluginsDir(projectRoot.resolve(pluginHome))
            .build()

        pluginManager = PluginManager(config).apply { start() }

        val localPath = Paths.get(
            System.getProperty("user.dir"),
            "src", "test", "kotlin", "io", "edurt", "datacap", "test", "BaseServiceTest.kt"
        ).toString()

        log.info("local path [ {} ]", localPath)
        request = FsRequest.builder()
            .access(System.getProperty("$pluginPrefix.access"))
            .secret(System.getProperty("$pluginPrefix.secret"))
            .bucket(System.getProperty("$pluginPrefix.bucket"))
            .endpoint(System.getProperty("$pluginPrefix.endpoint"))
            .stream(FileInputStream(localPath))
            .fileName(fileName)
            .build()
    }

    private fun getPlugin(): Optional<Plugin>
    {
        val plugin = pluginManager.getPlugin(pluginName)
        assertNotNull(plugin.get())
        return plugin
    }

    @Test
    fun step1_plugin()
    {
        val plugin = pluginManager.getPlugin(pluginName)
        assertNotNull(plugin.get())
    }

    @Test
    fun step2_writer()
    {
        val plugin = getPlugin()
        plugin.ifPresent {
            val service = it.getService(FsService::class.java)
            val response = service.writer(request)
            assertTrue(response.isSuccessful)
        }
    }

    @Test
    fun step3_reader()
    {
        val plugin = getPlugin()
        plugin.ifPresent {
            val service = it.getService(FsService::class.java)
            val response = service.reader(request)
            log.info("$pluginName reader response [ {} ]", response)
            assertTrue(response.isSuccessful)
        }
    }

    @Test
    fun step4_delete()
    {
        val plugin = getPlugin()
        plugin.ifPresent {
            val service = it.getService(FsService::class.java)
            val response = service.delete(request)
            log.info("$pluginName delete response [ {} ]", response)
            assertTrue(response.isSuccessful)
        }
    }
}
