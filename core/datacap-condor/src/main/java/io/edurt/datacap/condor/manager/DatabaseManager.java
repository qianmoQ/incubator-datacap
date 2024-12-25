package io.edurt.datacap.condor.manager;

import io.edurt.datacap.condor.DatabaseException;
import io.edurt.datacap.condor.metadata.DatabaseDefinition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseManager
{
    private static final String ROOT_DIR = "data";
    private Map<String, DatabaseDefinition> databases;
    private String currentDatabase;

    public DatabaseManager()
    {
        this.databases = new HashMap<>();
        initializeRootDirectory();
        loadExistingDatabases();
    }

    private void initializeRootDirectory()
    {
        File directory = new File(ROOT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void loadExistingDatabases()
    {
        File rootDir = new File(ROOT_DIR);
        File[] dbDirs = rootDir.listFiles(File::isDirectory);
        if (dbDirs != null) {
            for (File dbDir : dbDirs) {
                String dbName = dbDir.getName();
                databases.put(dbName, new DatabaseDefinition(dbName));
            }
        }
    }

    public void createDatabase(String databaseName)
            throws DatabaseException
    {
        // 验证数据库名称
        validateDatabaseName(databaseName);

        // 检查数据库是否已存在
        if (databases.containsKey(databaseName)) {
            throw new DatabaseException("Database '" + databaseName + "' already exists");
        }

        // 创建数据库目录
        try {
            Path dbPath = Paths.get(ROOT_DIR, databaseName);
            Files.createDirectory(dbPath);

            // 创建必要的子目录
            Files.createDirectory(dbPath.resolve("tables"));
            Files.createDirectory(dbPath.resolve("metadata"));

            // 创建并保存数据库配置
            Properties dbConfig = new Properties();
            dbConfig.setProperty("created_time", String.valueOf(System.currentTimeMillis()));
            dbConfig.setProperty("version", "1.0");

            Path configPath = dbPath.resolve("metadata/db.properties");
            dbConfig.store(Files.newOutputStream(configPath), "Database Configuration");

            // 创建数据库对象并添加到管理器
            DatabaseDefinition database = new DatabaseDefinition(databaseName);
            databases.put(databaseName, database);

            // 设置为当前数据库
            currentDatabase = databaseName;
        }
        catch (IOException e) {
            throw new DatabaseException("Failed to create database: " + e.getMessage());
        }
    }

    private void validateDatabaseName(String name)
            throws DatabaseException
    {
        if (name == null || name.trim().isEmpty()) {
            throw new DatabaseException("Database name cannot be empty");
        }

        // 检查数据库名称的合法性
        if (!name.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new DatabaseException("Invalid database name. Database name must start with a letter and can only contain letters, numbers, and underscores");
        }

        // 检查长度限制
        if (name.length() > 64) {
            throw new DatabaseException("Database name is too long (maximum 64 characters)");
        }
    }

    public void dropDatabase(String databaseName)
            throws DatabaseException
    {
        if (!databases.containsKey(databaseName)) {
            throw new DatabaseException("Database '" + databaseName + "' does not exist");
        }

        try {
            // 删除数据库目录及其所有内容
            Path dbPath = Paths.get(ROOT_DIR, databaseName);
            Files.walk(dbPath)
                    .sorted((p1, p2) -> -p1.compareTo(p2)) // 反向排序，确保先删除文件再删除目录
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            // 从管理器中移除数据库
            databases.remove(databaseName);

            // 如果删除的是当前数据库，重置当前数据库
            if (databaseName.equals(currentDatabase)) {
                currentDatabase = null;
            }
        }
        catch (IOException e) {
            throw new DatabaseException("Failed to drop database: " + e.getMessage());
        }
    }

    public void useDatabase(String databaseName)
            throws DatabaseException
    {
        if (!databases.containsKey(databaseName)) {
            throw new DatabaseException("Database '" + databaseName + "' does not exist");
        }
        currentDatabase = databaseName;
    }

    public DatabaseDefinition getCurrentDatabase()
            throws DatabaseException
    {
        if (currentDatabase == null) {
            throw new DatabaseException("No database selected");
        }
        return databases.get(currentDatabase);
    }

    public boolean databaseExists(String databaseName)
    {
        return databases.containsKey(databaseName);
    }

    public String[] listDatabases()
    {
        return databases.keySet().toArray(new String[0]);
    }
}
