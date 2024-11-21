package io.edurt.datacap.plugin;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.datacap.common.utils.DateUtils;
import io.edurt.datacap.plugin.loader.PluginLoaderFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class PluginManager
{
    // 插件配置
    // Plugin configuration
    private final PluginConfig config;

    // 插件存储映射
    // Plugin storage mapping
    private final Map<String, PluginMetadata> plugins;

    // 运行状态标志
    // Running state flag
    private volatile boolean running;

    public PluginManager(PluginConfig config)
    {
        this.config = config;
        this.plugins = Maps.newConcurrentMap();
    }

    // 启动插件管理器
    // Start plugin manager
    public void start()
    {
        running = true;
        createPluginsDirectoryIfNotExists();
        loadPlugins();

        if (config.isAutoReload()) {
            startPluginWatcher();
        }
    }

    // 停止插件管理器
    // Stop plugin manager
    public void stop()
    {
        running = false;
        plugins.values().forEach(this::closePluginClassLoader);
        plugins.clear();
    }

    // 创建插件目录（如果不存在）
    // Create plugins directory if not exists
    private void createPluginsDirectoryIfNotExists()
    {
        if (!config.getPluginsDir().toFile().isFile()) {
            try {
                Files.createDirectories(config.getPluginsDir());
            }
            catch (IOException e) {
                log.warn("Failed to create plugins directory", e);
            }
        }
    }

    // 加载所有插件
    // Load all plugins
    private void loadPlugins()
    {
        try {
            Path pluginsPath = config.getPluginsDir();

            if (Files.isRegularFile(pluginsPath)) {
                // 如果是文件直接加载
                // Load plugin from file
                loadPluginFromDirectory(pluginsPath);
            }
            else {
                // 如果是目录则遍历加载
                // Load plugins from directory
                try (Stream<Path> paths = Files.walk(pluginsPath, config.getScanDepth())) {
                    paths.filter(Files::isDirectory)
                            .peek(path -> log.debug("Scanning plugin directory: {}", path))
                            .filter(path -> !path.equals(pluginsPath))
                            .forEach(this::loadPluginFromDirectory);
                }
            }
        }
        catch (IOException e) {
            log.error("Failed to scan plugins directory", e);
        }
    }

    // 从目录加载插件
    // Load plugin from directory
    private void loadPluginFromDirectory(Path pluginDir)
    {
        try {
            List<Plugin> modules = PluginLoaderFactory.loadPlugins(pluginDir);

            for (Plugin module : modules) {
                String pluginName = module.getName();

                // 为每个插件模块创建独立的注入器
                // Create separate injector for each plugin module
                Injector pluginInjector = Guice.createInjector(module);
                module.setInjector(pluginInjector);

                PluginMetadata pluginMetadata = PluginMetadata.builder()
                        .name(pluginName)
                        .version(module.getVersion())
                        .location(pluginDir)
                        .state(PluginState.CREATED)
                        .classLoader(module.getClass().getClassLoader())
                        .loaderName(module.getClassLoader())
                        .instance(module)
                        .type(module.getType())
                        .loadTimestamp(System.currentTimeMillis())
                        .loadTime(DateUtils.formatYMDHMSWithInterval())
                        .build();

                // 移除旧版本插件
                // Remove old version plugin
                PluginMetadata oldPlugin = plugins.remove(pluginName);
                if (oldPlugin != null) {
                    closePluginClassLoader(oldPlugin);
                }

                plugins.put(pluginName, pluginMetadata);

                log.info("Install plugin: [ {} ] type [ {} ] version [ {} ] loader [ {} ] from source [ {} ]",
                        pluginName, module.getType().getName(), module.getVersion(), pluginMetadata.getLoaderName(), pluginDir);
            }
        }
        catch (Exception e) {
            log.error("Failed to load plugin from directory: {}", pluginDir, e);
        }
    }

    // 关闭插件类加载器
    // Close plugin class loader
    private void closePluginClassLoader(PluginMetadata pluginMetadata)
    {
        try {
            if (pluginMetadata.getClassLoader() instanceof URLClassLoader) {
                ((URLClassLoader) pluginMetadata.getClassLoader()).close();
            }
        }
        catch (IOException e) {
            log.error("Failed to close plugin classloader: {}", pluginMetadata.getName(), e);
        }
    }

    // 启动插件监视器线程
    // Start plugin watcher thread
    private void startPluginWatcher()
    {
        Thread watchThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(config.getScanInterval());
                    loadPlugins();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        watchThread.setDaemon(true);
        watchThread.start();
    }

    // 获取指定名称的插件
    // Get plugin by name
    public Optional<Plugin> getPlugin(String name)
    {
        return Optional.ofNullable(plugins.get(name))
                .map(info -> (Plugin) info.getInstance());
    }

    // 获取所有插件信息
    // Get all plugin information
    public List<PluginMetadata> getPluginInfos()
    {
        return new ArrayList<>(plugins.values());
    }

    // 卸载指定名称的插件
    // Unload plugin by name
    public boolean unloadPlugin(String name)
    {
        PluginMetadata pluginMetadata = plugins.remove(name);
        if (pluginMetadata != null) {
            closePluginClassLoader(pluginMetadata);
            return true;
        }
        return false;
    }
}
