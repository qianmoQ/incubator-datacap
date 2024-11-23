package io.edurt.datacap.plugin.loader;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.SpiType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressFBWarnings(value = {"MS_OOI_PKGPROTECT"})
public interface PluginLoader
{
    Set<String> EXCLUDED_DIRS = new HashSet<>()
    {{
        // Maven 相关目录
        // Maven related directories
        add("target/test-classes");
        add("target/generated-sources");
        add("target/generated-test-sources");
        add("target/maven-status");
        add("target/maven-archiver");
        add("target/surefire-reports");
        add("target/reports");
        add("target/javadoc-bundle-options");

        // 版本控制和 IDE 目录
        // Version control and IDE directories
        add(".git");
        add(".idea");
        add(".vscode");
        add(".settings");
        add(".project");
        add(".classpath");

        // 依赖目录
        // Dependency directories
        add("node_modules");

        // SPI 特有的排除目录
        // SPI-specific exclusion directories
        add("META-INF/services/test");
        add("META-INF/maven");
        add("META-INF/versions");

        // 源码目录
        // Source code directory
        add("src/main/java");
        add("src/main/kotlin");
        add("src/main/resources");
        add("src/test");
        add("src/test/java");
        add("src/test/kotlin");
        add("src/test/resources");
    }};

    /**
     * 检查路径是否应该被排除
     * Check if the path should be excluded
     */
    default boolean isExcludedPath(Path path)
    {
        String pathStr = path.toString();
        return EXCLUDED_DIRS.stream()
                .anyMatch(dir -> pathStr.contains(dir.replace('/', File.separatorChar)));
    }

    /**
     * 检查目录是否有效且不在排除列表中
     * Check if the directory is valid and not in the exclusion list
     */
    default boolean isValidDirectory(Path path)
    {
        return Files.exists(path) &&
                Files.isDirectory(path) &&
                !isExcludedPath(path);
    }

    // 获取加载器类型
    // Get loader type
    SpiType getType();

    // 加载插件
    // Load plugins
    List<Plugin> load(Path path);
}
