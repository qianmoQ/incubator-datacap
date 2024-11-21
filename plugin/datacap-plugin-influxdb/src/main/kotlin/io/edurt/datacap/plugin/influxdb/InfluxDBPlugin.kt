package io.edurt.datacap.plugin.influxdb

import io.edurt.datacap.spi.PluginService

class InfluxDBPlugin : _root_ide_package_.io.edurt.datacap.spi.PluginService
{
    override fun validator(): String
    {
        return "SELECT 1"
    }

    override fun driver(): String
    {
        return "net.suteren.jdbc.influxdb.InfluxDbDriver"
    }

    override fun connectType(): String
    {
        return "influxdb"
    }
}
