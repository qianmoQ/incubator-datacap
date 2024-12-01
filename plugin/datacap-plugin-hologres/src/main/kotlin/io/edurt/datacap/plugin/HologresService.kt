package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService

class HologresService : PluginService
{
    override fun connectType(): String
    {
        return "postgresql"
    }

    override fun driver(): String
    {
        return "org.postgresql.Driver"
    }
}