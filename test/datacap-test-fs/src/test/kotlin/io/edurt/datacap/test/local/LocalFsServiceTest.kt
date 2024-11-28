package io.edurt.datacap.test.local

import io.edurt.datacap.fs.FsRequest
import io.edurt.datacap.fs.FsService
import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.test.assertTrue

class LocalFsServiceTest
{
    private val log = getLogger(this::class.java)
    private val pluginManager: PluginManager
    private val pluginName = "LocalFs"
    private val fileName = "LocalFsPluginTest.kt"
    private val request: FsRequest

    init
    {
        val projectRoot = PluginPathUtils.findProjectRoot()
        val config = PluginConfigure.builder()
            .pluginsDir(projectRoot.resolve("fs/datacap-fs-local"))
            .build()

        pluginManager = PluginManager(config).apply { start() }

        request = FsRequest.builder()
            .access(null)
            .secret(null)
            .endpoint(listOf(System.getProperty("user.dir"), "data").joinToString(File.separator))
            .bucket("tmp")
            .localPath(
                listOf(
                    System.getProperty("user.dir"),
                    "src/test/kotlin/io/edurt/datacap/test/local/LocalFsPluginTest.kt"
                ).joinToString(File.separator)
            )
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
    fun step3_writer()
    {
        val plugin = getPlugin()

        plugin.ifPresent {
            val service = it.getService(FsService::class.java)
            val response = service.writer(request)
            assertTrue(response.isSuccessful)

            log.info("LocalFs writer response [ {} ]", response)
        }
    }

    @Test
    fun step4_reader()
    {
        val plugin = getPlugin()

        plugin.ifPresent {
            val service = it.getService(FsService::class.java)
            val response = service.reader(request)
            assertTrue(response.isSuccessful)

            log.info("====== [ {} ] ======", response.remote)
            try
            {
                BufferedReader(InputStreamReader(response.context, StandardCharsets.UTF_8)).use { reader ->
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null)
                    {
                        log.info(line)
                    }
                }
            }
            catch (e: IOException)
            {
                log.error("Reader error", e)
            }
        }
    }

    @Test
    fun step5_delete()
    {
        val plugin = getPlugin()
        plugin.ifPresent {
            val service = it.getService(FsService::class.java)
            val response = service.delete(request)
            assertTrue(response.isSuccessful)

            log.info("LocalFs delete response [ {} ]", response)
        }
    }
}
