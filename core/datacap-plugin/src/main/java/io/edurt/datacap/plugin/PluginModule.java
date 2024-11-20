package io.edurt.datacap.plugin;

import com.google.inject.AbstractModule;

public class PluginModule
        extends AbstractModule
{
    String getName()
    {
        return this.getClass().getSimpleName()
                .replace("PluginModule", "")
                .replace("Module", "");
    }

    String getVersion()
    {
        return this.getClass()
                .getPackage()
                .getImplementationVersion();
    }

    String getType()
    {
        return "Plugin";
    }
}
