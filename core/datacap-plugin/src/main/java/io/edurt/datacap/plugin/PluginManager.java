package io.edurt.datacap.plugin;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.datacap.plugin.loader.CompiledPomPluginLoader;
import io.edurt.datacap.plugin.loader.DirectoryPluginLoader;
import io.edurt.datacap.plugin.loader.PluginLoader;
import io.edurt.datacap.plugin.loader.PluginLoaderFactory;
import io.edurt.datacap.plugin.loader.PomPluginLoader;
import io.edurt.datacap.plugin.loader.SPIPluginLoader;
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
    private final Map<String, PluginInfo> plugins;

    // Guice注入器
    // Guice injector
    private final Injector injector;

    // 插件加载器列表
    // Plugin loaders list
    private final List<PluginLoader> loaders;

    // 运行状态标志
    // Running state flag
    private volatile boolean running;

    public PluginManager(PluginConfig config)
    {
        this.config = config;
        this.plugins = Maps.newConcurrentMap();
        this.injector = Guice.createInjector(new PluginModule());

        // 注册插件加载器
        // Register plugin loaders
        this.loaders = List.of(
                new SPIPluginLoader(),
                new PomPluginLoader(),
                new DirectoryPluginLoader(),
                new CompiledPomPluginLoader()
        );
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
        try {
            Files.createDirectories(config.getPluginsDir());
        }
        catch (IOException e) {
            log.warn("Failed to create plugins directory", e);
        }
    }

    // 加载所有插件
    // Load all plugins
    private void loadPlugins()
    {
        try (Stream<Path> paths = Files.walk(config.getPluginsDir(), 1)) {
            paths.filter(Files::isDirectory)
                    .peek(path -> log.info("Scanning plugin directory: {}", path))
                    .filter(path -> !path.equals(config.getPluginsDir()))
                    .forEach(this::loadPluginFromDirectory);
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
            List<PluginModule> modules = PluginLoaderFactory.loadPlugins(pluginDir);

            for (PluginModule module : modules) {
                String pluginName = module.getName();
                log.info("Found plugin module: [ {} ] type [ {} ]", pluginName, module.getType());

                PluginInfo pluginInfo = PluginInfo.builder()
                        .name(pluginName)
                        .version(module.getVersion())
                        .location(pluginDir)
                        .state(PluginState.CREATED)
                        .classLoader(module.getClass().getClassLoader())
                        .instance(module)
                        .loadTime(System.currentTimeMillis())
                        .build();

                // 移除旧版本插件
                // Remove old version plugin
                PluginInfo oldPlugin = plugins.remove(pluginName);
                if (oldPlugin != null) {
                    closePluginClassLoader(oldPlugin);
                }

                plugins.put(pluginName, pluginInfo);
                log.info("Successfully loaded plugin: [ {} ] from directory [ {} ]",
                        pluginName, pluginDir);
            }
        }
        catch (Exception e) {
            log.error("Failed to load plugin from directory: {}", pluginDir, e);
        }
    }

    // 关闭插件类加载器
    // Close plugin class loader
    private void closePluginClassLoader(PluginInfo pluginInfo)
    {
        try {
            if (pluginInfo.getClassLoader() instanceof URLClassLoader) {
                ((URLClassLoader) pluginInfo.getClassLoader()).close();
            }
        }
        catch (IOException e) {
            log.error("Failed to close plugin classloader: {}", pluginInfo.getName(), e);
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
    public Optional<PluginModule> getPlugin(String name)
    {
        return Optional.ofNullable(plugins.get(name))
                .map(info -> (PluginModule) info.getInstance());
    }

    // 获取所有插件信息
    // Get all plugin information
    public List<PluginInfo> getPluginInfos()
    {
        return new ArrayList<>(plugins.values());
    }

    // 卸载指定名称的插件
    // Unload plugin by name
    public boolean unloadPlugin(String name)
    {
        PluginInfo pluginInfo = plugins.remove(name);
        if (pluginInfo != null) {
            closePluginClassLoader(pluginInfo);
            return true;
        }
        return false;
    }
}
