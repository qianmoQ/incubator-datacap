package io.edurt.datacap.plugin.loader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 插件专用类加载器
 * Plugin-specific ClassLoader
 */
@Slf4j
public class PluginClassLoader
        extends URLClassLoader
{
    @Getter
    private final String name;

    @Getter
    private final String pluginName;

    @Getter
    private final String pluginVersion;

    public PluginClassLoader(URL[] urls, ClassLoader parent, String pluginName, String pluginVersion)
    {
        super(urls, parent);
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.name = String.join("-", "loader", pluginName.toLowerCase(), pluginVersion.toLowerCase());
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
                        name.startsWith("org.")) {
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
}
