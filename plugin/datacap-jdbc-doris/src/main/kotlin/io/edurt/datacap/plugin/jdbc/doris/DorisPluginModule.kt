package io.edurt.datacap.plugin.jdbc.doris

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.PluginService
import io.edurt.datacap.spi.PluginModule
import io.edurt.datacap.spi.PluginType

class DorisPluginModule : AbstractPluginModule(), PluginModule {
    override fun getName(): String {
        return "Doris"
    }

    override fun getType(): PluginType {
        return PluginType.JDBC
    }

    override fun get(): AbstractPluginModule {
        return this
    }

    override fun configure() {
        val module = Multibinder.newSetBinder(binder(), String::class.java)
        module.addBinding().toInstance(this.javaClass.simpleName)
        val plugin: Multibinder<_root_ide_package_.io.edurt.datacap.spi.PluginService> = Multibinder.newSetBinder(binder(), _root_ide_package_.io.edurt.datacap.spi.PluginService::class.java)
        plugin.addBinding().to(DorisPlugin::class.java)
    }
}