package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

public class ElasticSearch8xService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "es";
    }

    @Override
    public String driver()
    {
        return "org.elasticsearch.xpack.sql.jdbc.EsDriver";
    }
}
