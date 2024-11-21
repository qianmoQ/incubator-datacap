package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.PluginService;

import java.util.Set;

public class MySQLPlugin
        extends Plugin
{
    @Override
    public Set<Class<? extends Service>> getServiceTypes()
    {
        return Set.of(Service.class, PluginService.class);
    }
}
