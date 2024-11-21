package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class MySQLService
    implements PluginService
{
    @Override
    public String connectType()
    {
        return "mysql";
    }

    @Override
    public String driver()
    {
        return "com.mysql.cj.jdbc.Driver";
    }
}
