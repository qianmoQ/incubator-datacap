package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService

class PinotService : PluginService
{
    override fun validator(): String
    {
        return "select * from billing limit 10"
    }

    override fun connectType(): String
    {
        return "pinot"
    }

    override fun driver(): String
    {
        return "org.apache.pinot.client.PinotDriver"
    }
}
