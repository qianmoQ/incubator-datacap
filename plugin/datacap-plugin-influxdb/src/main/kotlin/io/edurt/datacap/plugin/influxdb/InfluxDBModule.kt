package io.edurt.datacap.plugin.influxdb

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.PluginService
import io.edurt.datacap.spi.PluginModule

class InfluxDBModule : AbstractPluginModule(), PluginModule
{
    override fun get(): AbstractPluginModule
    {
        return this
    }

    override fun configure()
    {
        Multibinder.newSetBinder(binder(), _root_ide_package_.io.edurt.datacap.spi.PluginService::class.java)
            .addBinding()
            .to(InfluxDBPlugin::class.java)
    }
}
