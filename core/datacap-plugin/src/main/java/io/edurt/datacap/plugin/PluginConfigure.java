package io.edurt.datacap.plugin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Builder
public class PluginConfigure
{
    @Setter
    private Path pluginsDir;
    private boolean autoReload;
    private long scanInterval;
    private String pluginConfigFile;

    // 扫描层级，只有目录情况下才会生效
    // Scan depth, only effective when scanning directory
    private int scanDepth;

    // 自动清理, 只有卸载时才会生效
    // Auto cleanup, only effective when unloading
    public boolean autoCleanup;

    // 同一目录下多个插件是否共享类加载器
    // Whether multiple plugins in the same directory share the class loader
    public boolean shareClassLoaderWhenSameDir;

    public static PluginConfigure defaultConfig()
    {
        return PluginConfigure.builder()
                .pluginsDir(Paths.get("plugins"))
                .autoReload(false)
                .scanInterval(5000)
                .pluginConfigFile("plugin.properties")
                .scanDepth(1)
                .autoCleanup(true)
                .build();
    }
}
