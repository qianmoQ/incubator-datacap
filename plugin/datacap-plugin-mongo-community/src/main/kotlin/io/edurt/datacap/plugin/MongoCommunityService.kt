package io.edurt.datacap.plugin

import io.edurt.datacap.spi.PluginService

class MongoCommunityService : PluginService
{
    override fun connectType(): String
    {
        return "mongodb"
    }

    override fun driver(): String
    {
        return "com.dbschema.MongoJdbcDriver"
    }
}
