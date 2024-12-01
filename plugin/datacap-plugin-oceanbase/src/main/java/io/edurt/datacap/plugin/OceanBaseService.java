package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class OceanBaseService
        implements PluginService
{
    @Override
    public String driver()
    {
        return "com.oceanbase.jdbc.Driver";
    }

    @Override
    public String connectType()
    {
        return "oceanbase";
    }
}
