package io.edurt.datacap.test.mongo;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MongoJdbcDriverTest
{
    @Test
    public void test()
    {
        try
        {
            Class.forName("io.edurt.datacap.driver.MongoJdbcDriver");

            Properties props = new Properties();
            props.setProperty("authDatabase", "admin");
            props.setProperty("user", "mongoadmin");
            props.setProperty("password", "secret");
            props.setProperty("database", "local");
            Connection conn = DriverManager.getConnection("jdbc:mongodb://localhost:27017", props);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT hostname, startTime FROM startup_log");

            while (rs.next())
            {
                System.out.println(rs.getString("name"));
            }
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }
}
