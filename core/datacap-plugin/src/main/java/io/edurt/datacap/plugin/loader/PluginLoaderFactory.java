package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.PluginModule;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PluginLoaderFactory
{
    // 用于缓存已注册的加载器
    // Cache for registered loaders
    private static final Map<String, PluginLoader> loaderRegistry = new ConcurrentHashMap<>();

    // 注册默认的加载器
    // Register default loaders
    static {
        registerLoader(new SPIPluginLoader());
        registerLoader(new PomPluginLoader());
        registerLoader(new DirectoryPluginLoader());
        registerLoader(new CompiledPomPluginLoader());
    }

    /**
     * 注册一个新的插件加载器
     * Register a new plugin loader
     *
     * @param loader 要注册的加载器
     * @param loader the loader to register
     */
    public static void registerLoader(PluginLoader loader)
    {
        String type = loader.getType();
        if (type == null || type.trim().isEmpty()) {
            log.warn("Attempted to register loader with null or empty type: {}", loader.getClass().getName());
            return;
        }

        PluginLoader existing = loaderRegistry.putIfAbsent(type.toLowerCase(), loader);
        if (existing != null) {
            log.warn("Loader type '{}' is already registered, skipping registration of {}",
                    type, loader.getClass().getName());
        }
        else {
            log.info("Registered plugin loader: {} for type: {}", loader.getClass().getName(), type);
        }
    }

    /**
     * 根据类型获取加载器
     * Get loader by type
     *
     * @param type 加载器类型
     * @param type loader type
     * @return 对应的加载器实例，如果未找到则返回null
     * @return corresponding loader instance, or null if not found
     */
    public static PluginLoader getLoader(String type)
    {
        if (type == null) {
            return null;
        }
        return loaderRegistry.get(type.toLowerCase());
    }

    /**
     * 使用所有已注册的加载器尝试加载插件
     * Attempt to load plugins using all registered loaders
     *
     * @param pluginDir 插件目录
     * @param pluginDir plugin directory
     * @return 加载的插件模块列表
     * @return list of loaded plugin modules
     */
    public static List<PluginModule> loadPlugins(Path pluginDir)
    {
        if (pluginDir == null) {
            log.warn("Plugin directory is null");
            return Collections.emptyList();
        }

        // 遍历所有注册的加载器尝试加载
        // Iterate through all registered loaders to attempt loading
        for (Map.Entry<String, PluginLoader> entry : loaderRegistry.entrySet()) {
            String type = entry.getKey();
            PluginLoader loader = entry.getValue();

            try {
                List<PluginModule> modules = loader.load(pluginDir);
                if (modules != null && !modules.isEmpty()) {
                    log.info("Successfully loaded {} plugin(s) using loader type: {}", modules.size(), type);
                    return modules;
                }
            }
            catch (Exception e) {
                log.debug("Failed to load plugins using loader type '{}': {}", type, e.getMessage());
            }
        }

        log.warn("No plugins could be loaded from directory: {}", pluginDir);
        return Collections.emptyList();
    }

    /**
     * 获取所有已注册的加载器类型
     * Get all registered loader types
     *
     * @return 加载器类型列表
     * @return list of loader types
     */
    public static List<String> getRegisteredTypes()
    {
        return Collections.unmodifiableList(
                new ArrayList<>(loaderRegistry.keySet())
        );
    }
}
