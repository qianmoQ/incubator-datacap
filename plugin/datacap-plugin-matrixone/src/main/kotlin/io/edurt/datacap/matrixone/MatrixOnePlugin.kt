package io.edurt.datacap.matrixone

import io.edurt.datacap.spi.PluginService

class MatrixOnePlugin : _root_ide_package_.io.edurt.datacap.spi.PluginService
{
    override fun driver(): String {
        return "com.mysql.cj.jdbc.Driver"
    }

    override fun connectType(): String {
        return "mysql"
    }
}
