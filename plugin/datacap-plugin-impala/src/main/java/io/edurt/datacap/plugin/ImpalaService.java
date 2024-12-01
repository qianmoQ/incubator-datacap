package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class ImpalaService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "impala";
    }

    @Override
    public String driver()
    {
        return "com.cloudera.impala.jdbc.Driver";
    }
}
