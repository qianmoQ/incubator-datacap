package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService
import io.edurt.datacap.spi.model.Configure
import java.util.*

class MongoCommunityService : PluginService
{
    override fun connectType(): String
    {
        return "mongodb"
    }

    override fun url(configure: Configure): String
    {
        val buffer = StringBuilder()
        buffer.append("jdbc:")
        buffer.append(configure.type)
        buffer.append("://")
        buffer.append(configure.host)
        buffer.append(":")
        buffer.append(configure.port)

        val properties = Properties()
        configure.database.ifPresent { properties["database"] = it }
        configure.env.ifPresent { properties.putAll(it) }

        return buffer.toString()
    }

    override fun driver(): String
    {
        return "com.dbschema.MongoJdbcDriver"
    }
}
