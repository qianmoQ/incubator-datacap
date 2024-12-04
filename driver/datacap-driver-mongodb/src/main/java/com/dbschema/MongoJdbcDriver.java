package com.dbschema;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MongoJdbcDriver
        extends io.edurt.datacap.driver.MongoJdbcDriver
{
    static {
        try {
            DriverManager.registerDriver(new MongoJdbcDriver());
        }
        catch (SQLException e) {
            throw new RuntimeException("Can't register com.dbschema.MongoJdbcDriver", e);
        }
    }
}
