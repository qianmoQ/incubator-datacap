package io.edurt.datacap.test;

import io.edurt.datacap.plugin.Plugin;

public class LocalPlugin
        implements Plugin
{
    @Override
    public String name()
    {
        return "Local";
    }

    @Override
    public String version()
    {
        return "1.0.0";
    }
}
