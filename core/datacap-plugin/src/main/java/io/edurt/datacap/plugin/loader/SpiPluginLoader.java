package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.PluginContextManager;
import io.edurt.datacap.plugin.SpiType;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import io.edurt.datacap.plugin.utils.VersionUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class SpiPluginLoader
        implements PluginLoader
{
    @Override
    public SpiType getType()
    {
        return SpiType.SPI;
    }

    @Override
    public List<Plugin> load(Path path)
    {
        try {
            // 获取目录名作为插件名
            // Get directory name as plugin name
            String pluginName = path.getFileName().toString();
            String version = VersionUtils.determinePluginVersion(path);

            // 创建插件专用类加载器
            // Create plugin-specific class loader
            PluginClassLoader classLoader = PluginClassLoaderUtils.createClassLoader(
                    path,
                    pluginName,
                    version
            );

            return PluginContextManager.runWithClassLoader(classLoader, () -> {
                ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class, classLoader);
                List<Plugin> plugins = StreamSupport.stream(serviceLoader.spliterator(), false)
                        .collect(Collectors.toList());

                // 设置插件的类加载器
                // Set class loader for plugins
                plugins.forEach(plugin -> plugin.setPluginClassLoader(classLoader));

                return plugins;
            });
        }
        catch (Exception e) {
            log.error("Failed to load plugins using SPI from: {}", path, e);
            return List.of();
        }
    }
}
