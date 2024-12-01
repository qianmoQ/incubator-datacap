package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService

class TimescaleService : PluginService
{
    override fun driver(): String
    {
        return "org.postgresql.Driver"
    }

    override fun connectType(): String
    {
        return "postgresql"
    }
}
