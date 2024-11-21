package io.edurt.datacap.plugin;

public enum PluginType
{
    CONNECTOR("Connector"),
    EXECUTOR("Executor"),
    SCHEDULER("Scheduler"),
    CONVERT("Convert");

    private String name;

    PluginType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
