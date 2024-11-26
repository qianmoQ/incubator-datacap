package io.edurt.datacap.test.spi

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.PluginType
import io.edurt.datacap.plugin.annotation.InjectPlugin

@InjectPlugin
class NoneExecutorPlugin : Plugin()
{
    override fun getType(): PluginType
    {
        return PluginType.EXECUTOR
    }
}
