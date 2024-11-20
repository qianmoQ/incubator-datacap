package io.edurt.datacap.plugin;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

@Data
@Builder
public class PluginInfo
{
    private String name;
    private String version;
    private Path location;
    private PluginState state;
    private ClassLoader classLoader;
    private Object instance;
    private long loadTime;
}
