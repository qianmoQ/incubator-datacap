package io.edurt.datacap.test.mongo;

import io.edurt.datacap.driver.MongoQueryParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MongoQueryParserTest
{
    private final String sql = "SELECT name, age FROM users WHERE age > 18 AND city = 'Beijing' ORDER BY age DESC LIMIT 10";
    private MongoQueryParser parser;

    @Before
    public void before()
    {
        this.parser = new MongoQueryParser(sql);
    }

    @Test
    public void testSelect()
    {
        System.out.println("Collection: " + parser.getCollection());  // users
        System.out.println("Query: " + parser.getQuery());           // {age: {$gt: 18}, city: "Beijing"}
        System.out.println("Sort: " + parser.getSort());             // {age: -1}
        System.out.println("Limit: " + parser.getLimit());           // 10

        assertEquals("users", parser.getCollection());
        assertEquals("{\"$and\": [{\"age\": {\"$gt\": 18}}, {\"city\": \"Beijing\"}]}", parser.getQuery().toJson());
        assertEquals("{\"age\": -1}", parser.getSort().toJson());
        assertEquals(10, parser.getLimit());
    }
}
