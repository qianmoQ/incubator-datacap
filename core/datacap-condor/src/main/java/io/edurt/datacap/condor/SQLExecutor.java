package io.edurt.datacap.condor;

import io.edurt.datacap.condor.manager.DatabaseManager;
import io.edurt.datacap.sql.SQLParser;
import io.edurt.datacap.sql.statement.CreateDatabaseStatement;
import io.edurt.datacap.sql.statement.DropDatabaseStatement;
import io.edurt.datacap.sql.statement.SQLStatement;
import io.edurt.datacap.sql.statement.UseDatabaseStatement;

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

            if (statement instanceof DropDatabaseStatement) {
                DropDatabaseStatement dropDatabaseStatement = (DropDatabaseStatement) statement;
                return executeDropDatabase(dropDatabaseStatement);
            }

            if (statement instanceof UseDatabaseStatement) {
                UseDatabaseStatement useDatabaseStatement = (UseDatabaseStatement) statement;
                return executeUseDatabase(useDatabaseStatement);
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

    private SQLResult executeDropDatabase(DropDatabaseStatement statement)
    {
        try {
            if (statement.isIfNotExists() && !databaseManager.databaseExists(statement.getDatabaseName())) {
                return new SQLResult(true, "Database does not exist");
            }

            databaseManager.dropDatabase(statement.getDatabaseName());
            return new SQLResult(true, "Database dropped successfully");
        }
        catch (DatabaseException e) {
            return new SQLResult(false, "Failed to drop database: " + e.getMessage());
        }
    }

    private SQLResult executeUseDatabase(UseDatabaseStatement statement)
    {
        try {
            String databaseName = statement.getDatabaseName();
            databaseManager.useDatabase(databaseName);
            return new SQLResult(true, "Database changed");
        }
        catch (DatabaseException e) {
            return new SQLResult(false, "Failed to use database: " + e.getMessage());
        }
    }
}
