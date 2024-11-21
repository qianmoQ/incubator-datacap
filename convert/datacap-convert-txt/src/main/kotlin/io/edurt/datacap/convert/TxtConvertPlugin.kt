package io.edurt.datacap.convert

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginType

class TxtConvertPlugin : Plugin()
{
    override fun getType(): PluginType
    {
        return PluginType.CONVERT
    }
}
