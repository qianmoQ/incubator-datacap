package io.edurt.datacap.plugin;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.datacap.common.utils.DateUtils;
import io.edurt.datacap.plugin.loader.PluginClassLoader;
import io.edurt.datacap.plugin.loader.PluginLoaderFactory;
import io.edurt.datacap.plugin.utils.PluginClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Manifest;
import java.util.stream.Stream;

@Slf4j
public class PluginManager
{
    // 插件配置
    // Plugin configuration
    private final PluginConfigure config;

    // 插件存储映射
    // Plugin storage mapping
    private final Map<String, PluginMetadata> plugins;

    // 插件类加载器映射
    // Plugin class loader mapping
    private final Map<String, PluginClassLoader> pluginClassLoaders;

    // 运行状态标志
    // Running state flag
    private volatile boolean running;

    // 插件安装锁
    // Plugin installation lock
    private final Object installLock = new Object();

    // 临时目录前缀
    // Temporary directory prefix
    private static final String TEMP_DIR_PREFIX = "plugin_install_";

    public PluginManager(PluginConfigure config)
    {
        this.config = config;
        this.plugins = Maps.newConcurrentMap();
        this.pluginClassLoaders = Maps.newConcurrentMap();
    }

    // 启动插件管理器
    // Start plugin manager
    public void start()
    {
        running = true;
        createPluginsDirectoryIfNotExists();
        loadPlugins();

        if (config.isAutoReload()) {
            startPluginWatcher();
        }
    }

    // 停止插件管理器
    // Stop plugin manager
    public void stop()
    {
        running = false;

        // 关闭所有插件的类加载器
        // Close class loaders for all plugins
        plugins.values().forEach(this::closePluginClassLoader);

        // 清理映射
        // Clean up mappings
        pluginClassLoaders.clear();
        plugins.clear();
    }

    // 创建插件目录（如果不存在）
    // Create plugins directory if not exists
    private void createPluginsDirectoryIfNotExists()
    {
        if (!config.getPluginsDir().toFile().isFile()) {
            try {
                Files.createDirectories(config.getPluginsDir());
            }
            catch (IOException e) {
                log.warn("Failed to create plugins directory", e);
            }
        }
    }

    // 验证目录名称合法性
    // Validate directory name
    private boolean isValidDirectoryName(String name)
    {
        return name.matches("^[a-zA-Z0-9][a-zA-Z0-9_.-]*$");
    }

    // 安装新插件
    // Install new plugin
    public boolean installPlugin(Path sourcePath, String targetDirectory)
    {
        // 参数验证
        // Parameter validation
        if (sourcePath == null || targetDirectory == null || targetDirectory.trim().isEmpty()) {
            throw new IllegalArgumentException("Source path and target directory cannot be null or empty");
        }

        // 验证源路径是否存在
        // Verify source path exists
        if (!Files.exists(sourcePath)) {
            log.error("Source plugin path does not exist: {}", sourcePath);
            return false;
        }

        // 验证目标目录名称合法性
        // Validate target directory name
        if (!isValidDirectoryName(targetDirectory)) {
            log.error("Invalid target directory name: {}", targetDirectory);
            return false;
        }

        synchronized (installLock) {
            Path tempDir = null;
            boolean installed;

            try {
                // 创建临时目录
                // Create temporary directory
                tempDir = Files.createTempDirectory(config.getPluginsDir(), TEMP_DIR_PREFIX);

                // 根据插件类型处理安装
                // Handle installation based on plugin type
                installed = processPluginInstallation(sourcePath, tempDir);

                if (installed) {
                    // 创建最终目标目录
                    // Create final target directory
                    Path targetPath = config.getPluginsDir().resolve(targetDirectory);

                    // 如果目标目录已存在，先备份
                    // Backup existing target directory if it exists
                    if (Files.exists(targetPath)) {
                        backupExistingPlugin(targetPath);
                    }

                    // 将临时目录移动到最终位置
                    // Move temporary directory to final location
                    moveDirectory(tempDir, targetPath);

                    // 加载新安装的插件
                    // Load newly installed plugin
                    loadPluginFromDirectory(targetPath);

                    log.info("Successfully installed plugin from {} to {}", sourcePath, targetPath);
                    return true;
                }

                return false;
            }
            catch (Exception e) {
                log.error("Failed to install plugin from: {} to {}", sourcePath, targetDirectory, e);
                return false;
            }
            finally {
                // 清理临时目录
                // Clean up temporary directory
                if (tempDir != null) {
                    deleteDirectory(tempDir);
                }
            }
        }
    }

    // 处理插件安装
    // Process plugin installation
    private boolean processPluginInstallation(Path sourcePath, Path tempDir)
            throws IOException
    {
        String extension = FilenameUtils.getExtension(sourcePath.toString()).toLowerCase();
        AtomicBoolean installed = new AtomicBoolean(false);

        // 检测并处理插件类型
        // Detect and handle plugin type
        try {
            // Properties 插件
            // Properties plugin
            if ("properties".equals(extension)) {
                installed.set(installPropertiesPlugin(sourcePath, tempDir));
            }
            // POM 插件
            // POM plugin
            else if (isPomPlugin(sourcePath)) {
                installed.set(installPomPlugin(sourcePath, tempDir));
            }
            // 编译后的插件
            // Compiled plugin
            else if ("class".equals(extension) || "jar".equals(extension)) {
                installed.set(installCompiledPlugin(sourcePath, tempDir));
            }
            // 目录类型插件
            // Directory plugin
            else if (Files.isDirectory(sourcePath)) {
                installed.set(installDirectoryPlugin(sourcePath, tempDir));
            }
            // SPI 类型插件
            // SPI plugin
            else if (isSpiPlugin(sourcePath)) {
                installed.set(installSpiPlugin(sourcePath, tempDir));
            }
            else {
                log.warn("Unsupported plugin type for: {}", sourcePath);
            }

            // 验证安装后的插件结构
            // Validate installed plugin structure
            if (installed.get()) {
                validatePluginStructure(tempDir);
            }
        }
        catch (Exception e) {
            log.error("Failed to process plugin installation: {}", sourcePath, e);
            throw e;
        }

        return installed.get();
    }

    // 备份现有插件
    // Backup existing plugin
    private void backupExistingPlugin(Path pluginPath)
            throws IOException
    {
        if (Files.exists(pluginPath)) {
            String timestamp = DateUtils.formatYMDHMSWithInterval();
            Path backupPath = pluginPath.getParent().resolve(pluginPath.getFileName() + ".backup." + timestamp);

            try {
                Files.move(pluginPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("Created backup of existing plugin: {}", backupPath);
            }
            catch (IOException e) {
                log.error("Failed to create backup of existing plugin: {}", pluginPath, e);
                throw e;
            }
        }
    }

    // 移动目录
    // Move directory
    private void moveDirectory(Path source, Path target)
            throws IOException
    {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (FileAlreadyExistsException e) {
            // 如果移动失败，尝试复制
            // If move fails, try copy
            copyDirectory(source, target);
        }
    }

    // 删除目录
    // Delete directory
    private void deleteDirectory(Path directory)
    {
        try {
            if (Files.exists(directory)) {
                Files.walk(directory)
                        .sorted((a, b) -> b.compareTo(a))
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
        catch (IOException e) {
            log.warn("Failed to delete directory: {}", directory, e);
        }
    }

    // 验证插件结构
    // Validate plugin structure
    private void validatePluginStructure(Path pluginPath)
            throws IOException
    {
        // 验证基本文件结构
        // Validate basic file structure
        boolean isValid = Files.exists(pluginPath) &&
                (Files.exists(pluginPath.resolve("pom.xml")) ||
                        Files.exists(pluginPath.resolve("plugin.properties")) ||
                        Files.exists(pluginPath.resolve("META-INF/services")) ||
                        Files.list(pluginPath).anyMatch(path -> path.toString().endsWith(".class")) ||
                        Files.list(pluginPath).anyMatch(path -> path.toString().endsWith(".jar")));

        if (!isValid) {
            throw new IOException("Invalid plugin structure in: " + pluginPath);
        }
    }

    // 检查是否为 POM 插件
    // Check if it's a POM plugin
    private boolean isPomPlugin(Path path)
    {
        return path.toString().endsWith("pom.xml") ||
                (Files.isDirectory(path) && Files.exists(path.resolve("pom.xml")));
    }

    // 检查是否为 SPI 插件
    // Check if it's a SPI plugin
    private boolean isSpiPlugin(Path path)
    {
        if (Files.isDirectory(path)) {
            try {
                Path servicesPath = path.resolve("META-INF/services");
                return Files.exists(servicesPath) &&
                        Files.list(servicesPath)
                                .anyMatch(file -> file.toString().endsWith(Plugin.class.getName()));
            }
            catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    // 安装 Properties 类型插件
    // Install Properties type plugin
    private boolean installPropertiesPlugin(Path sourcePath, Path targetPath)
            throws IOException
    {
        Files.copy(sourcePath, targetPath.resolve("plugin.properties"),
                StandardCopyOption.REPLACE_EXISTING);
        return true;
    }

    // 安装 POM 类型插件
    // Install POM type plugin
    private boolean installPomPlugin(Path sourcePath, Path targetPath)
            throws IOException
    {
        if (Files.isDirectory(sourcePath)) {
            copyDirectory(sourcePath, targetPath);
        }
        else {
            Files.copy(sourcePath, targetPath.resolve("pom.xml"),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return true;
    }

    // 安装编译后的插件
    // Install compiled plugin
    private boolean installCompiledPlugin(Path sourcePath, Path targetPath)
            throws IOException
    {
        if (Files.isDirectory(sourcePath)) {
            copyDirectory(sourcePath, targetPath);
        }
        else {
            Files.copy(sourcePath, targetPath.resolve(sourcePath.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return true;
    }

    // 安装目录类型插件
    // Install directory type plugin
    private boolean installDirectoryPlugin(Path sourcePath, Path targetPath)
            throws IOException
    {
        copyDirectory(sourcePath, targetPath);
        return true;
    }

    // 安装 SPI 类型插件
    // Install SPI type plugin
    private boolean installSpiPlugin(Path sourcePath, Path targetPath)
            throws IOException
    {
        copyDirectory(sourcePath, targetPath);
        return true;
    }

    // 复制目录
    // Copy directory
    private void copyDirectory(Path source, Path target)
            throws IOException
    {
        try (Stream<Path> stream = Files.walk(source)) {
            stream.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    }
                    else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                catch (IOException e) {
                    log.error("Failed to copy file: {} to {}", sourcePath, target, e);
                }
            });
        }
    }

    // 加载所有插件
    // Load all plugins
    private void loadPlugins()
    {
        try {
            Path pluginsPath = config.getPluginsDir();

            if (Files.isRegularFile(pluginsPath)) {
                log.debug("Loading plugin from file: {}", pluginsPath);
                // 如果是文件直接加载
                // Load plugin from file
                loadPluginFromDirectory(pluginsPath);
            }
            else {
                // 如果是目录则遍历加载
                // Load plugins from directory
                try (Stream<Path> paths = Files.walk(pluginsPath, config.getScanDepth())) {
                    paths.filter(Files::isDirectory)
                            .peek(path -> log.debug("Scanning plugin directory: {}", path))
                            .filter(path -> !path.equals(pluginsPath))
                            .forEach(this::loadPluginFromDirectory);
                }
            }
        }
        catch (IOException e) {
            log.error("Failed to scan plugins directory", e);
        }
    }

    // 从目录加载插件
    // Load plugin from directory
    private void loadPluginFromDirectory(Path pluginDir)
    {
        try {
            // 从目录名获取插件基本信息
            // Get plugin basic information from directory name
            String pluginBaseName = pluginDir.getFileName().toString();
            log.debug("Found plugin directory: {}", pluginDir);
            log.debug("Found plugin: {}", pluginBaseName);

            // 获取插件版本(可以从配置文件或清单文件中读取)
            // Get plugin version (can be read from config or manifest file)
            String pluginVersion = getPluginVersion(pluginDir);
            log.debug("Found plugin version: {}", pluginVersion);

            // 创建插件专用类加载器
            // Create plugin-specific class loader
            PluginClassLoader loader = PluginClassLoaderUtils.createClassLoader(
                    pluginDir,
                    pluginBaseName,
                    pluginVersion
            );

            List<Plugin> modules = PluginContextManager.runWithClassLoader(loader, () -> PluginLoaderFactory.loadPlugins(pluginDir));

            for (Plugin module : modules) {
                PluginContextManager.runWithClassLoader(loader, () -> {
                    // 为每个插件模块创建独立的注入器
                    // Create separate injector for each plugin module
                    Injector pluginInjector = Guice.createInjector(module);
                    module.setInjector(pluginInjector);

                    String pluginName = module.getName();
                    // 保存类加载器信息
                    // Save class loader information
                    pluginClassLoaders.put(pluginName, loader);

                    PluginMetadata pluginMetadata = PluginMetadata.builder()
                            .name(pluginName)
                            .version(module.getVersion())
                            .location(pluginDir)
                            .state(PluginState.CREATED)
                            .classLoader(loader)
                            .loaderName(module.getClassLoader())
                            .instance(module)
                            .type(module.getType())
                            .loadTimestamp(System.currentTimeMillis())
                            .loadTime(DateUtils.formatYMDHMSWithInterval())
                            .build();

                    // 移除旧版本插件
                    // Remove old version plugin
                    PluginMetadata oldPlugin = plugins.remove(pluginName);
                    if (oldPlugin != null) {
                        closePluginClassLoader(oldPlugin);
                    }

                    plugins.put(pluginName, pluginMetadata);

                    log.info("Install plugin: [ {} ] type [ {} ] version [ {} ] loader [ {} ] from source [ {} ]",
                            pluginName, module.getType().getName(), module.getVersion(), pluginMetadata.getLoaderName(), pluginDir);

                    return null;
                });
            }
        }
        catch (Exception e) {
            log.error("Failed to load plugin from directory: {}", pluginDir, e);
        }
    }

    // 获取插件版本
    // Get plugin version
    private String getPluginVersion(Path pluginDir)
    {
        // 这里可以实现从配置文件或清单文件中读取版本号的逻辑
        // Here you can implement logic to read version number from config or manifest file
        try {
            // 优先从 plugin.properties 读取
            // Read from plugin.properties first
            Path propertiesFile = pluginDir.resolve("plugin.properties");
            if (Files.exists(propertiesFile)) {
                // 读取属性文件实现
                // Implement properties file reading
                return readVersionFromProperties(propertiesFile);
            }

            // 其次从 MANIFEST.MF 读取
            // Then read from MANIFEST.MF
            Path manifestFile = pluginDir.resolve("META-INF/MANIFEST.MF");
            if (Files.exists(manifestFile)) {
                return readVersionFromManifest(manifestFile);
            }

            // 最后从 pom.xml 读取
            // Finally read from pom.xml
            Path pomFile = pluginDir.resolve("pom.xml");
            if (Files.exists(pomFile)) {
                return readVersionFromPom(pomFile);
            }
        }
        catch (Exception e) {
            log.warn("Failed to read plugin version from: {}", pluginDir, e);
        }

        // 如果都读取失败，返回默认版本
        // Return default version if all reads fail
        return "1.0.0";
    }

    private String readVersionFromProperties(Path propertiesFile)
    {
        // TODO: 实现从 properties 文件读取版本号
        // Implement reading version from properties file
        return "1.0.0";
    }

    private String readVersionFromManifest(Path manifestFile)
            throws IOException
    {
        // TODO: 实现从 MANIFEST.MF 读取版本号
        // Implement reading version from MANIFEST.MF
        ClassLoader loader = getClass().getClassLoader();

        Enumeration<URL> resources = loader.getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            try (InputStream is = resources.nextElement().openStream()) {
                Manifest manifest = new Manifest(is);
                String version = manifest.getMainAttributes().getValue("Implementation-Version");
                if (version != null) {
                    return version;
                }
            }
        }
        return "1.0.0";
    }

    private String readVersionFromPom(Path pomFile)
    {
        // TODO: 实现从 pom.xml 读取版本号
        // Implement reading version from pom.xml
        return "1.0.0";
    }

    // 关闭插件类加载器
    // Close plugin class loader
    private void closePluginClassLoader(PluginMetadata pluginMetadata)
    {
        try {
            String pluginName = pluginMetadata.getName();

            // 从映射中移除
            // Remove from mapping
            PluginClassLoader removed = pluginClassLoaders.remove(pluginName);

            if (removed != null) {
                removed.close();
                log.info("Closed class loader for plugin: {}", pluginName);
            }

            // 如果不是 PluginClassLoader，尝试关闭普通的 URLClassLoader
            // If not PluginClassLoader, try to close normal URLClassLoader
            if (pluginMetadata.getClassLoader() instanceof PluginClassLoader) {
                ((PluginClassLoader) pluginMetadata.getClassLoader()).close();
            }
        }
        catch (IOException e) {
            log.error("Failed to close plugin classloader: {}", pluginMetadata.getName(), e);
        }
    }

    // 启动插件监视器线程
    // Start plugin watcher thread
    private void startPluginWatcher()
    {
        Thread watchThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(config.getScanInterval());
                    loadPlugins();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        watchThread.setDaemon(true);
        watchThread.start();
    }

    // 获取指定名称的插件
    // Get plugin by name
    public Optional<Plugin> getPlugin(String name)
    {
        return Optional.ofNullable(plugins.get(name))
                .map(info -> (Plugin) info.getInstance());
    }

    // 获取插件的类加载器
    // Get plugin's class loader
    public Optional<ClassLoader> getPluginClassLoader(String pluginName)
    {
        return Optional.ofNullable(pluginClassLoaders.get(pluginName));
    }

    // 获取当前已加载的所有插件类加载器
    // Get all currently loaded plugin class loaders
    public Map<String, PluginClassLoader> getPluginClassLoaders()
    {
        return Maps.newHashMap(pluginClassLoaders);
    }

    // 获取所有插件信息
    // Get all plugin information
    public List<PluginMetadata> getPluginInfos()
    {
        return new ArrayList<>(plugins.values());
    }

    // 卸载指定名称的插件
    // Unload plugin by name
    public boolean uninstallPlugin(String name)
    {
        if (name == null || name.trim().isEmpty()) {
            log.warn("Plugin name cannot be null or empty");
            return false;
        }

        synchronized (installLock) {
            try {
                // 获取插件元数据
                // Get plugin metadata
                PluginMetadata pluginMetadata = plugins.get(name);
                if (pluginMetadata == null) {
                    log.warn("Plugin not found: {}", name);
                    return false;
                }

                // 检查插件状态
                // Check plugin state
                if (pluginMetadata.getState() == PluginState.UNLOADED) {
                    log.warn("Plugin {} is already unloaded", name);
                    return false;
                }

                // 创建卸载备份
                // Create unload backup
                Path pluginLocation = pluginMetadata.getLocation();
                if (pluginLocation != null && Files.exists(pluginLocation)) {
                    createUnloadBackup(pluginLocation);
                }

                // 清理插件资源
                // Clean up plugin resources
                try {
                    // 关闭类加载器
                    // Close class loader
                    closePluginClassLoader(pluginMetadata);

                    // 清理注入器
                    // Clean up injector
                    Plugin plugin = (Plugin) pluginMetadata.getInstance();
                    if (plugin != null && plugin.getInjector() != null) {
                        // Close any resources managed by the injector
                        try {
                            plugin.getInjector().getInstance(AutoCloseable.class);
                        }
                        catch (Exception ignored) {
                            // Ignore if no AutoCloseable is bound
                        }
                    }

                    // 更新插件状态
                    // Update plugin state
                    pluginMetadata.setState(PluginState.UNLOADED);

                    // 从插件映射中移除
                    // Remove from plugin mapping
                    plugins.remove(name);

                    // 清理插件文件（如果配置了自动清理）
                    // Clean up plugin files (if auto cleanup is configured)
                    if (config.isAutoCleanup()) {
                        deletePluginFiles(pluginLocation);
                    }

                    log.info("Successfully unloaded plugin: {}", name);
                    return true;
                }
                catch (Exception e) {
                    log.error("Error while unloading plugin: {}", name, e);
                    // 尝试强制移除
                    // Try force removal
                    plugins.remove(name);
                    return false;
                }
            }
            catch (Exception e) {
                log.error("Failed to unload plugin: {}", name, e);
                return false;
            }
        }
    }

    // 创建卸载备份
    // Create unload backup
    private void createUnloadBackup(Path pluginLocation)
    {
        try {
            String timestamp = DateUtils.formatYMDHMSWithInterval();
            Path backupPath = pluginLocation.getParent()
                    .resolve(pluginLocation.getFileName() + ".unload." + timestamp);

            if (Files.isDirectory(pluginLocation)) {
                copyDirectory(pluginLocation, backupPath);
            }
            else {
                Files.copy(pluginLocation, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Created unload backup at: {}", backupPath);
        }
        catch (IOException e) {
            log.warn("Failed to create unload backup for: {}", pluginLocation, e);
        }
    }

    // 删除插件文件
    // Delete plugin files
    private void deletePluginFiles(Path pluginLocation)
    {
        if (pluginLocation == null || !Files.exists(pluginLocation)) {
            return;
        }

        try {
            if (Files.isDirectory(pluginLocation)) {
                deleteDirectory(pluginLocation);
            }
            else {
                Files.delete(pluginLocation);
            }
            log.info("Deleted plugin files at: {}", pluginLocation);
        }
        catch (IOException e) {
            log.warn("Failed to delete plugin files at: {}", pluginLocation, e);
        }
    }

    // 强制卸载插件
    // Force unload plugin
    public boolean forceUnloadPlugin(String name)
    {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        synchronized (installLock) {
            try {
                PluginMetadata pluginMetadata = plugins.remove(name);
                if (pluginMetadata != null) {
                    try {
                        closePluginClassLoader(pluginMetadata);
                    }
                    catch (Exception e) {
                        log.warn("Error while force closing plugin classloader: {}", name, e);
                    }

                    try {
                        if (config.isAutoCleanup()) {
                            deletePluginFiles(pluginMetadata.getLocation());
                        }
                    }
                    catch (Exception e) {
                        log.warn("Error while force deleting plugin files: {}", name, e);
                    }

                    log.info("Force unloaded plugin: {}", name);
                    return true;
                }
                return false;
            }
            catch (Exception e) {
                log.error("Failed to force unload plugin: {}", name, e);
                return false;
            }
        }
    }
}
