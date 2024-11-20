package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URLClassLoader;
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
    public String getType()
    {
        return "SPI";
    }

    @Override
    public List<Plugin> load(Path path)
    {
        try {
            URLClassLoader classLoader = PluginClassLoaderUtils.createClassLoader(path);
            ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class, classLoader);
            return StreamSupport.stream(serviceLoader.spliterator(), false)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            log.error("Failed to load plugins using SPI from: {}", path, e);
            return List.of();
        }
    }
}
