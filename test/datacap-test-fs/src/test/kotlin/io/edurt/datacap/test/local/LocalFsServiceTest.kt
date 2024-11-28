package io.edurt.datacap.test.local

import io.edurt.datacap.fs.FsRequest
import io.edurt.datacap.fs.FsService
import io.edurt.datacap.plugin.PluginConfigure
import io.edurt.datacap.plugin.PluginManager
import io.edurt.datacap.plugin.utils.PluginPathUtils
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import java.io.File
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

    @Test
    fun test()
    {
        val plugin = pluginManager.getPlugin(pluginName)
        assertNotNull(plugin.get())
    }

    @Test
    fun testWriter()
    {
        val plugin = pluginManager.getPlugin(pluginName)
        assertNotNull(plugin.get())

        plugin.ifPresent {
            val service = it.getService(FsService::class.java)

            val response = service.writer(request)
            log.info("LocalFs writer response [ {} ]", response)
            assertTrue(response.isSuccessful)
        }
    }
}
