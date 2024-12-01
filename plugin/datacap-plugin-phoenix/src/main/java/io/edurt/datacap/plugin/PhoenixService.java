package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class PhoenixService
        implements PluginService
{
    public PhoenixService()
    {
        System.setProperty("phoenix.monitoring.enabled", "false");
    }

    @Override
    public String driver()
    {
        return "org.apache.phoenix.jdbc.PhoenixDriver";
    }

    @Override
    public String connectType()
    {
        return "phoenix";
    }
}
