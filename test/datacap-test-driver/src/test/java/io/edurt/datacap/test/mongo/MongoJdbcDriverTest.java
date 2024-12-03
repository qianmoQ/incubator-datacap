package io.edurt.datacap.test.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class MongoJdbcDriverTest
{
    private Statement statement;
    private GenericContainer<?> container;
    private Connection connection;
    private MongoClient mongoClient;

    @Before
    public void init()
    {
        try {
            container = new GenericContainer(DockerImageName.parse("mongo"))
                    .withExposedPorts(27017)
                    .withEnv("MONGO_INITDB_ROOT_USERNAME", "mongoadmin")
                    .withEnv("MONGO_INITDB_ROOT_PASSWORD", "secret")
                    .withCommand("mongod", "--noauth")
                    .waitingFor(Wait.forListeningPort()
                            .withStartupTimeout(Duration.ofSeconds(30)))
                    .withCommand("mongod", "--auth");
            container.setPortBindings(List.of("27017:27017"));
            container.start();

            // Initialize test data using MongoDB Java Driver
            String mongoUri = String.format("mongodb://mongoadmin:secret@%s:%d",
                    container.getHost(),
                    container.getFirstMappedPort());
            mongoClient = MongoClients.create(mongoUri);
            MongoDatabase database = mongoClient.getDatabase("test");
            database.createCollection("sample");
            database.getCollection("sample").insertMany(
                    List.of(
                            new Document("name", "test1").append("value", 1),
                            new Document("name", "test2").append("value", 2)
                    )
            );

            // Initialize JDBC connection
            Class.forName("io.edurt.datacap.driver.MongoJdbcDriver");
            Properties props = new Properties();
            props.setProperty("database", "admin");
            props.setProperty("user", "mongoadmin");
            props.setProperty("password", "secret");

            String jdbcUrl = String.format("jdbc:mongodb://%s:%d",
                    container.getHost(),
                    container.getFirstMappedPort());

            connection = DriverManager.getConnection(jdbcUrl, props);
            statement = connection.createStatement();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to initialize test environment", e);
        }
    }

    @Test
    public void testShow()
            throws SQLException
    {
        try (ResultSet rs = statement.executeQuery("SHOW DATABASES")) {
            assertTrue(rs.next());
        }

        try (ResultSet rs = statement.executeQuery("SHOW TABLES FROM test")) {
            assertTrue(rs.next());
        }

        try (ResultSet rs = statement.executeQuery("SHOW COLUMNS FROM test.sample")) {
            assertTrue(rs.next());
        }
    }

    @Test
    public void testSelect()
            throws SQLException
    {
        try (ResultSet rs = statement.executeQuery("SELECT * FROM test.sample")) {
            assertTrue(rs.next());
        }

        try (ResultSet rs = statement.executeQuery("SELECT * FROM test.sample WHERE value = 2")) {
            assertTrue(rs.next());
        }

        try (ResultSet rs = statement.executeQuery("SELECT name FROM test.sample WHERE value = 2")) {
            assertTrue(rs.next());
        }
    }

    @After
    public void cleanup()
    {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (mongoClient != null) {
                mongoClient.close();
            }
            if (container != null) {
                container.stop();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
