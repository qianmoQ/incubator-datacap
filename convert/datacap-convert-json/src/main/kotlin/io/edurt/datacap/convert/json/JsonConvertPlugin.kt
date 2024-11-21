package io.edurt.datacap.convert.json

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginType

class JsonConvertPlugin : Plugin()
{
    override fun getType(): PluginType
    {
        return PluginType.CONVERT
    }
}
