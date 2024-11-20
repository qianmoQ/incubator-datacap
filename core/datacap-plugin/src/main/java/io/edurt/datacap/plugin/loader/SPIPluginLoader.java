package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.PluginModule;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class SPIPluginLoader
        implements PluginLoader
{
    @Override
    public String getType()
    {
        return "SPI";
    }

    @Override
    public List<PluginModule> load(Path path)
    {
        try {
            URLClassLoader classLoader = PluginClassLoaderUtils.createClassLoader(path);
            ServiceLoader<PluginModule> serviceLoader = ServiceLoader.load(PluginModule.class, classLoader);
            return StreamSupport.stream(serviceLoader.spliterator(), false)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            log.error("Failed to load plugins using SPI from: {}", path, e);
            return List.of();
        }
    }
}
