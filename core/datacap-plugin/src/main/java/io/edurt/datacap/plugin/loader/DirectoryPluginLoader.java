package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.SpiType;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class DirectoryPluginLoader
        implements PluginLoader
{
    @Override
    public SpiType getType()
    {
        return SpiType.DIRECTORY;
    }

    @Override
    public List<Plugin> load(Path path)
    {
        try {
            // 获取目录名作为插件名
            // Get directory name as plugin name
            String pluginName = path.getFileName().toString();

            // 创建插件专用类加载器
            // Create plugin-specific class loader
            PluginClassLoader classLoader = PluginClassLoaderUtils.createClassLoader(
                    path,
                    pluginName,
                    "1.0.0"
            );

            List<Plugin> plugins = Files.walk(path)
                    .filter(p -> p.toString().endsWith(".class"))
                    .filter(p -> !p.toString().contains("$"))
                    .map(p -> loadClass(classLoader, p))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            // 设置插件的类加载器
            // Set class loader for plugins
            plugins.forEach(plugin -> plugin.setPluginClassLoader(classLoader));

            return plugins;
        }
        catch (Exception e) {
            log.error("Failed to load plugins from directory: {}", path, e);
            return List.of();
        }
    }

    private Optional<Plugin> loadClass(PluginClassLoader classLoader, Path classFile)
    {
        try {
            String className = getClassName(classFile);
            Class<?> cls = classLoader.loadClass(className);

            if (Plugin.class.isAssignableFrom(cls)) {
                Plugin plugin = (Plugin) cls.getDeclaredConstructor().newInstance();
                plugin.setPluginClassLoader(classLoader);
                return Optional.of(plugin);
            }
        }
        catch (Exception e) {
            log.debug("Failed to load class: {}", classFile, e);
        }
        return Optional.empty();
    }

    private String getClassName(Path classFile)
    {
        String path = classFile.toString();
        return path.substring(path.indexOf("io/edurt"))
                .replace("/", ".")
                .replace(".class", "");
    }
}
