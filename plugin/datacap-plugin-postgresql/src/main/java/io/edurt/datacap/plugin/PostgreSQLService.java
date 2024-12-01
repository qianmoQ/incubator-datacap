package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class PostgreSQLService
        implements PluginService
{
    @Override
    public String driver()
    {
        return "org.postgresql.Driver";
    }

    @Override
    public String connectType()
    {
        return "postgresql";
    }
}
