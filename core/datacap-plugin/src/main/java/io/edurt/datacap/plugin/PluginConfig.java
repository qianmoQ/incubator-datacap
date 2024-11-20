package io.edurt.datacap.plugin;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Builder
public class PluginConfig
{
    private Path pluginsDir;
    private boolean autoReload;
    private long scanInterval;
    private String pluginConfigFile;

    public static PluginConfig defaultConfig()
    {
        return PluginConfig.builder()
                .pluginsDir(Paths.get("plugins"))
                .autoReload(false)
                .scanInterval(5000)
                .pluginConfigFile("plugin.properties")
                .build();
    }
}
