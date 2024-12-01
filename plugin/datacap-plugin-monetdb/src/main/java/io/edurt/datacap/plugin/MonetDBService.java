package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class MonetDBService
        implements PluginService
{
    @Override
    public String driver()
    {
        return "nl.cwi.monetdb.jdbc.MonetDriver";
    }

    @Override
    public String connectType()
    {
        return "monetdb";
    }
}
