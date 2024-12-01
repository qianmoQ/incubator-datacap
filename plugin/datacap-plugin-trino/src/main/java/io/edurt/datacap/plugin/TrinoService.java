package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class TrinoService
        implements PluginService
{
    @Override
    public String driver()
    {
        return "io.trino.jdbc.TrinoDriver";
    }

    @Override
    public String connectType()
    {
        return "trino";
    }
}
