package io.edurt.datacap.condor.manager;

import io.edurt.datacap.condor.SQLExecutor;
import io.edurt.datacap.condor.SQLResult;
import io.edurt.datacap.condor.metadata.RowDefinition;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertTrue;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest
{
    private final DatabaseManager databaseManager = DatabaseManager.createManager();

    @Test
    public void step1CreateDatabase()
    {
        SQLExecutor executor = new SQLExecutor(databaseManager);
        assertTrue(executor.execute("CREATE DATABASE IF NOT EXISTS test").isSuccess());
    }

    @Test
    public void step2UseDatabase()
    {
        SQLExecutor executor = new SQLExecutor(databaseManager);
        assertTrue(executor.execute("USE DATABASE test").isSuccess());
    }

    @Test
    public void step3DropDatabase()
    {
        SQLExecutor executor = new SQLExecutor(databaseManager);
        assertTrue(executor.execute("DROP DATABASE IF EXISTS test").isSuccess());
    }

    @Test
    public void step4CreateTable()
    {
        SQLExecutor executor = new SQLExecutor(databaseManager);
        log.info("{}", executor.execute("USE test"));

        String sql = "DROP TABLE IF EXISTS test_table";
        log.info("{}", executor.execute(sql));

        sql = "CREATE TABLE IF NOT EXISTS test_table (id INT, name VARCHAR(255))";
        log.info("{}", executor.execute(sql));

        sql = "INSERT INTO test_table (id, name) VALUES (1, 'John')";
        log.info("{}", executor.execute(sql));

        sql = "INSERT INTO test_table (id, name) VALUES (1, 'John'), (2, 'Jane')";
        log.info("{}", executor.execute(sql));

        sql = "SELECT id, name FROM test_table";
        SQLResult<List<RowDefinition>> rows = executor.execute(sql);
        rows.getData().forEach(row -> log.info("{}", row.getValue("name")));
    }
}
