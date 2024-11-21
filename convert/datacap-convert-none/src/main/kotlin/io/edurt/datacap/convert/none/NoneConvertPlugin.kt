package io.edurt.datacap.convert.none

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginType

class NoneConvertPlugin : Plugin()
{
    override fun getType(): PluginType
    {
        return PluginType.CONVERT
    }
}
