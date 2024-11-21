package io.edurt.datacap.jdbc.hologres

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.PluginService
import io.edurt.datacap.spi.PluginModule
import io.edurt.datacap.spi.PluginType

class HologresPluginModule : AbstractPluginModule(), PluginModule {
    override fun getName(): String {
        return this.javaClass.simpleName.replace("PluginModule", "")
    }

    override fun getType(): PluginType {
        return PluginType.JDBC
    }

    override fun get(): AbstractPluginModule {
        return this
    }

    override fun configure() {
        val plugin: Multibinder<_root_ide_package_.io.edurt.datacap.spi.PluginService> = Multibinder.newSetBinder(binder(), _root_ide_package_.io.edurt.datacap.spi.PluginService::class.java)
        plugin.addBinding().to(HologresPlugin::class.java)
    }
}