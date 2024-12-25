package io.edurt.datacap.condor.manager;

import io.edurt.datacap.condor.SQLExecutor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DatabaseTest
{
    private final DatabaseManager databaseManager = new DatabaseManager();

    @Test
    public void testCreateDatabase()
    {
        SQLExecutor executor = new SQLExecutor(databaseManager);
        assertTrue(executor.execute("CREATE DATABASE IF NOT EXISTS test").isSuccess());
        assertTrue(executor.execute("CREATE DATABASE test").isSuccess());
    }
}
