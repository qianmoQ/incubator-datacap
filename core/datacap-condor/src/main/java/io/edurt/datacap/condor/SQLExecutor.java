package io.edurt.datacap.condor;

import io.edurt.datacap.condor.manager.DatabaseManager;
import io.edurt.datacap.sql.SQLParser;
import io.edurt.datacap.sql.statement.CreateDatabaseStatement;
import io.edurt.datacap.sql.statement.SQLStatement;

public class SQLExecutor
{
    private final DatabaseManager databaseManager;

    public SQLExecutor(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
    }

    public SQLResult execute(String sql)
    {
        try {
            SQLStatement statement = SQLParser.parse(sql);

            if (statement instanceof CreateDatabaseStatement) {
                CreateDatabaseStatement createDatabaseStatement = (CreateDatabaseStatement) statement;
                return executeCreateDatabase(createDatabaseStatement);
            }

            return new SQLResult(false, String.format("Unsupported SQL statement: %s", statement));
        }
        catch (Exception e) {
            return new SQLResult(false, e.getMessage());
        }
    }

    private SQLResult executeCreateDatabase(CreateDatabaseStatement statement)
    {
        try {
            String databaseName = statement.getDatabaseName();

            // 检查是否带有 IF NOT EXISTS
            // Check if IF NOT EXISTS is present
            if (statement.isIfNotExists() && databaseManager.databaseExists(databaseName)) {
                return new SQLResult(true, "Database already exists");
            }

            // 执行创建数据库
            // Execute database creation
            databaseManager.createDatabase(databaseName);
            return new SQLResult(true, "Database created successfully");
        }
        catch (DatabaseException e) {
            return new SQLResult(false, "Failed to create database: " + e.getMessage());
        }
    }
}
