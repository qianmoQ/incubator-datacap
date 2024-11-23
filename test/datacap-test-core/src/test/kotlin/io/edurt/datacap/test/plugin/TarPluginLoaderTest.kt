package io.edurt.datacap.test.plugin

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.loader.TarPluginLoader
import org.junit.Test
import java.nio.file.Path
import kotlin.test.assertTrue

class TarPluginLoaderTest
{
    private val tarUrl = "https://cdn.north.devlive.org/applications/datacap/plugins/2024.4.0-SNAPSHOT/convert/datacap-convert-txt-bin.tar.gz"

    @Test
    fun test()
    {
        val loader = TarPluginLoader()

        val plugins: List<Plugin> = loader.load(Path.of(tarUrl))
        assertTrue(plugins.isNotEmpty())
    }
}
