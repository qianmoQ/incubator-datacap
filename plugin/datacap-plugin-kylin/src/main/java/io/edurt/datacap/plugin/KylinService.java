package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class KylinService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "kylin";
    }

    @Override
    public String driver()
    {
        return "org.apache.kylin.jdbc.Driver";
    }
}
