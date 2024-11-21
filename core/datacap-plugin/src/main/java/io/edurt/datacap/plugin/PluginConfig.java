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
    // 扫描层级，只有目录情况下才会生效
    // Scan depth, only effective when scanning directory
    private int scanDepth;

    public static PluginConfig defaultConfig()
    {
        return PluginConfig.builder()
                .pluginsDir(Paths.get("plugins"))
                .autoReload(false)
                .scanInterval(5000)
                .pluginConfigFile("plugin.properties")
                .scanDepth(1)
                .build();
    }
}
