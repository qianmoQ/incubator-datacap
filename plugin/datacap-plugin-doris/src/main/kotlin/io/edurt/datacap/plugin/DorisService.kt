package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService

class DorisService : PluginService
{
    override fun connectType(): String
    {
        return "mysql"
    }

    override fun driver(): String
    {
        return "com.mysql.cj.jdbc.Driver"
    }
}
