package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class MongoAtlasService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "mongodb";
    }

    @Override
    public String driver()
    {
        return "com.mongodb.jdbc.MongoDriver";
    }
}
