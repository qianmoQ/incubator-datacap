package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class Hive2xService
        implements PluginService
{
    @Override
    public String driver()
    {
        return "org.apache.hive.jdbc.HiveDriver";
    }

    @Override
    public String connectType()
    {
        return "hive2";
    }
}
