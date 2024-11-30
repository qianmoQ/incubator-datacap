package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService

class MatrixOneService : PluginService
{
    override fun driver(): String {
        return "com.mysql.cj.jdbc.Driver"
    }

    override fun connectType(): String {
        return "mysql"
    }
}
