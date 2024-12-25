package io.edurt.datacap.condor.manager;

import io.edurt.datacap.condor.SQLExecutor;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest
{
    private final DatabaseManager databaseManager = new DatabaseManager();

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
}
