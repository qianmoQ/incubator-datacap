package io.edurt.datacap.test.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public abstract class MongoJdbcBaseTest
{
    // Static container instance shared by all test classes
    @ClassRule
    public static final GenericContainer<?> MONGO_CONTAINER = new GenericContainer(DockerImageName.parse("mongo"))
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "mongoadmin")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "secret")
            .withCommand("mongod", "--noauth")
            .waitingFor(Wait.forListeningPort()
                    .withStartupTimeout(Duration.ofSeconds(30)))
            .withCommand("mongod", "--auth");
    protected Statement statement;
    protected Connection connection;
    protected MongoClient mongoClient;

    @Before
    public void init()
    {
        try {
            initializeMongoData();
            initializeJdbcConnection();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to initialize test environment", e);
        }
    }

    private void initializeMongoData()
    {
        String mongoUri = String.format("mongodb://mongoadmin:secret@%s:%d",
                MONGO_CONTAINER.getHost(),
                MONGO_CONTAINER.getFirstMappedPort());
        mongoClient = MongoClients.create(mongoUri);

        // Clean up existing data
        MongoDatabase database = mongoClient.getDatabase("test");
        try {
            database.getCollection("sample").drop();
        }
        catch (Exception ignored) {
        }

        database.createCollection("sample");
        database.getCollection("sample").insertMany(
                List.of(
                        new Document("name", "test1").append("value", 1),
                        new Document("name", "test2").append("value", 2),
                        new Document("name", "test3").append("value", 3),
                        new Document("name", "test1").append("value", 4),
                        new Document("name", "test2").append("value", 2)
                )
        );
    }

    private void initializeJdbcConnection()
            throws Exception
    {
        Class.forName("io.edurt.datacap.driver.MongoJdbcDriver");
        Properties props = new Properties();
        props.setProperty("database", "admin");
        props.setProperty("user", "mongoadmin");
        props.setProperty("password", "secret");

        String jdbcUrl = String.format("jdbc:mongodb://%s:%d",
                MONGO_CONTAINER.getHost(),
                MONGO_CONTAINER.getFirstMappedPort()
        );
        connection = DriverManager.getConnection(jdbcUrl, props);
        statement = connection.createStatement();
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        MONGO_CONTAINER.setPortBindings(List.of("27017:27017"));
        MONGO_CONTAINER.start();
    }
}
