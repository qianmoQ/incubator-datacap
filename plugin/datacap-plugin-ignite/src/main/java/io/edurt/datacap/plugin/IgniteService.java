package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class IgniteService
        implements PluginService
{
    @Override
    public String validator()
    {
        return "SELECT '-' AS version";
    }

    @Override
    public String connectType()
    {
        return "ignite:thin";
    }

    @Override
    public String driver()
    {
        return "org.apache.ignite.IgniteJdbcThinDriver";
    }
}
