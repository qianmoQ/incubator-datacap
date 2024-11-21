package io.edurt.datacap.plugin.loader;

import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.SpiType;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class PomPluginLoader
        implements PluginLoader
{
    @Override
    public SpiType getType()
    {
        return SpiType.POM;
    }

    @Override
    public List<Plugin> load(Path path)
    {
        try {
            Path pomFile = path.resolve("pom.xml");
            if (!Files.exists(pomFile)) {
                return List.of();
            }

            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(pomFile.toFile()));

            String mainClass = model.getProperties().getProperty("plugin.class");
            if (mainClass == null) {
                return List.of();
            }

            URLClassLoader classLoader = PluginClassLoaderUtils.createClassLoader(path);
            Class<?> pluginClass = classLoader.loadClass(mainClass);

            if (!Plugin.class.isAssignableFrom(pluginClass)) {
                log.error("Class {} does not implement PluginModule", mainClass);
                return List.of();
            }

            Plugin plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();
            return List.of(plugin);
        }
        catch (Exception e) {
            log.error("Failed to load plugins using POM from: {}", path, e);
            return List.of();
        }
    }
}
