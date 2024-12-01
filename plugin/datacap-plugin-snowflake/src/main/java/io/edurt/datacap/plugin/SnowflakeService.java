package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class SnowflakeService
        implements PluginService
{
    @Override
    public String driver()
    {
        return "net.snowflake.client.jdbc.SnowflakeDriver";
    }

    @Override
    public String connectType()
    {
        return "snowflake";
    }
}
