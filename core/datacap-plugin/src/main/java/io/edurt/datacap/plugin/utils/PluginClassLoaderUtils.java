package io.edurt.datacap.plugin.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PluginClassLoaderUtils
{
    public static URLClassLoader createClassLoader(Path directory)
            throws Exception
    {
        List<URL> urls = new ArrayList<>();

        // Add all JARs in the directory
        if (Files.isDirectory(directory)) {
            Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .forEach(path -> {
                        try {
                            urls.add(path.toUri().toURL());
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return new URLClassLoader(
                urls.toArray(new URL[0]),
                PluginClassLoaderUtils.class.getClassLoader()
        );
    }
}
