package io.edurt.datacap.test;

import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.PluginType;
import io.edurt.datacap.plugin.annotation.InjectPlugin;

@InjectPlugin
public class AnnotationPlugin
        extends Plugin
{
    @Override
    public PluginType getType()
    {
        return PluginType.CONNECTOR;
    }
}
