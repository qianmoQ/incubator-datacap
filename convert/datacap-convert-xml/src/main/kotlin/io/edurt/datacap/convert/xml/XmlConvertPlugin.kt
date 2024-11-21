package io.edurt.datacap.convert.xml

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginType

class XmlConvertPlugin : Plugin()
{
    override fun getType(): PluginType
    {
        return PluginType.CONVERT
    }
}
