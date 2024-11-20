package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.PluginModule;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URLClassLoader;
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
    public String getType()
    {
        return "Directory";
    }

    @Override
    public List<PluginModule> load(Path path)
    {
        try {
            URLClassLoader classLoader = PluginClassLoaderUtils.createClassLoader(path);
            return Files.walk(path)
                    .filter(p -> p.toString().endsWith(".class"))
                    .filter(p -> !p.toString().contains("$"))
                    .map(p -> loadClass(classLoader, p))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            log.error("Failed to load plugins from directory: {}", path, e);
            return List.of();
        }
    }

    private Optional<PluginModule> loadClass(URLClassLoader classLoader, Path classFile)
    {
        try {
            String className = getClassName(classFile);
            Class<?> cls = classLoader.loadClass(className);

            if (PluginModule.class.isAssignableFrom(cls)) {
                return Optional.of((PluginModule) cls.getDeclaredConstructor().newInstance());
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
