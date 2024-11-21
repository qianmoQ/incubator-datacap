package io.edurt.datacap.plugin.paradedb

import io.edurt.datacap.spi.PluginService

class ParadeDBPlugin : _root_ide_package_.io.edurt.datacap.spi.PluginService
{
    override fun driver(): String {
        return "org.postgresql.Driver"
    }

    override fun connectType(): String {
        return "postgresql"
    }
}
