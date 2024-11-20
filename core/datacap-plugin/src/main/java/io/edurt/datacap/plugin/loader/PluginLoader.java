package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.Plugin;

import java.nio.file.Path;
import java.util.List;

public interface PluginLoader
{
    // 获取加载器类型
    // Get loader type
    String getType();

    // 加载插件
    // Load plugins
    List<Plugin> load(Path path);
}
