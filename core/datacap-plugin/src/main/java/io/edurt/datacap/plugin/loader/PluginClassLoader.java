package io.edurt.datacap.plugin.loader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * 插件专用类加载器
 * Plugin-specific ClassLoader
 */
@Slf4j
public class PluginClassLoader
        extends URLClassLoader
        implements AutoCloseable
{
    @Getter
    private final String name;

    @Getter
    private final String pluginName;

    @Getter
    private final String pluginVersion;

    private final boolean parentFirst;

    public PluginClassLoader(URL[] urls, ClassLoader parent, String pluginName, String pluginVersion, boolean parentFirst)
    {
        super(urls, parent);
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.parentFirst = parentFirst;
        this.name = String.join("-", "loader", pluginName.toLowerCase(), pluginVersion.toLowerCase());
        log.debug("Created PluginClassLoader for {} with URLs: {}", pluginName, Arrays.toString(urls));
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            if (log.isDebugEnabled()) {
                log.debug("Trying to load class: {} with loader: {}", name, getClass().getName());
            }

            // 首先检查该类是否已经加载
            // First, check if the class has already been loaded
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }

            try {
                // 系统类和框架类使用父加载器
                // Use parent loader for system classes and framework classes
                if (name.startsWith("java.") ||
                        name.startsWith("javax.") ||
                        name.startsWith("com.google.") ||
                        name.startsWith("org.") ||
                        // 添加 Plugin 相关的包到父优先加载列表
                        // Add Plugin related packages to parent-first list
                        (parentFirst && name.startsWith("io.edurt.datacap.plugin"))) {
                    return super.loadClass(name, resolve);
                }

                // 其他所有类先尝试从当前类加载器加载
                // Try current loader first for all other classes
                try {
                    Class<?> c = findClass(name);
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
                catch (ClassNotFoundException e) {
                    // 当前加载器找不到时尝试父加载器
                    // Try parent loader if not found in current loader
                    return super.loadClass(name, resolve);
                }
            }
            catch (ClassNotFoundException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Failed to load class: {} with both current and parent loader", name);
                }
                throw e;
            }
        }
    }

    @Override
    public void close()
            throws IOException
    {
        try {
            super.close();
            log.debug("Closed PluginClassLoader for plugin: {}", pluginName);
        }
        catch (IOException e) {
            log.error("Error closing PluginClassLoader for plugin: {}", pluginName, e);
            throw e;
        }
    }
}
