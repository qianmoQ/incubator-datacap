package io.edurt.datacap.plugin.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * 插件类加载器工具类
 * Plugin Class Loader Utility Class
 */
@Slf4j
public class PluginClassLoaderUtils
{
    /**
     * 创建一个新的类加载器
     * Create a new class loader
     *
     * @param directory 插件目录 Plugin directory
     * @return 新创建的类加载器 The newly created class loader
     * @throws Exception 创建类加载器时发生异常 Exception occurred when creating the class loader
     */
    public static URLClassLoader createClassLoader(Path directory)
            throws Exception
    {
        log.debug("Creating new class loader for plugin directory: {}", directory);
        Set<URL> urls = new HashSet<>();

        if (Files.isDirectory(directory)) {
            // 添加主插件JAR
            // Add the main plugin JAR
            Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .forEach(path -> addJarAndDependencies(path, urls));

            // 检查常见的依赖目录
            // Check common dependency directories
            addDependenciesFromDir(directory.resolve("lib"), urls);
            addDependenciesFromDir(directory.resolve("dependencies"), urls);
            addDependenciesFromDir(directory.resolve("target/dependencies"), urls);
        }

        log.debug("Created class loader with {} URLs", urls.size());
        return new URLClassLoader(
                urls.toArray(new URL[0]),
                PluginClassLoaderUtils.class.getClassLoader()
        );
    }

    /**
     * 从指定目录中添加依赖JAR
     * Add dependencies from the specified directory
     *
     * @param dir 依赖目录 Dependency directory
     * @param urls URL集合 URL set
     */
    private static void addDependenciesFromDir(Path dir, Set<URL> urls)
    {
        if (Files.isDirectory(dir)) {
            try {
                log.debug("Scanning dependency directory: {}", dir);
                Files.walk(dir)
                        .filter(path -> path.toString().endsWith(".jar"))
                        .forEach(path -> {
                            try {
                                urls.add(path.toUri().toURL());
                                log.debug("Added dependency: {}", path);
                            }
                            catch (Exception e) {
                                log.error("Failed to add dependency: {}", path, e);
                                throw new RuntimeException("Failed to add dependency: " + path, e);
                            }
                        });
            }
            catch (Exception e) {
                log.error("Failed to scan dependency directory: {}", dir, e);
                throw new RuntimeException("Failed to scan dependency directory: " + dir, e);
            }
        }
    }

    /**
     * 添加JAR文件及其依赖
     * Add a JAR file and its dependencies
     *
     * @param jarPath JAR文件路径 JAR file path
     * @param urls URL集合 URL set
     */
    private static void addJarAndDependencies(Path jarPath, Set<URL> urls)
    {
        try {
            urls.add(jarPath.toUri().toURL());
            log.debug("Added JAR: {}", jarPath);
        }
        catch (Exception e) {
            log.error("Failed to add JAR: {}", jarPath, e);
            throw new RuntimeException("Failed to add JAR: " + jarPath, e);
        }
    }
}
