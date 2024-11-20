package io.edurt.datacap.plugin;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.util.Set;

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
    private Set<String> dependencies;
    private long loadTime;
}
