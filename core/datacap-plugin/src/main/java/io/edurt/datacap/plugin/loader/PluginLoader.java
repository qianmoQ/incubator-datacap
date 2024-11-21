package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.SpiType;

import java.nio.file.Path;
import java.util.List;

public interface PluginLoader
{
    // 获取加载器类型
    // Get loader type
    SpiType getType();

    // 加载插件
    // Load plugins
    List<Plugin> load(Path path);
}
