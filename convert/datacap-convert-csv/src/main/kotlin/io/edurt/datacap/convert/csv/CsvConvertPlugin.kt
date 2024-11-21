package io.edurt.datacap.convert.csv

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginType

class CsvConvertPlugin : Plugin()
{
    override fun getType(): PluginType
    {
        return PluginType.CONVERT
    }
}
