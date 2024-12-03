package io.edurt.datacap.test.mongo;

import io.edurt.datacap.driver.parser.MongoParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MongoSelectParserTest
{
    private final String sql = "SELECT name, age FROM users WHERE age > 18 AND city = 'Beijing' ORDER BY age DESC LIMIT 10";
    private MongoParser parser;

    @Before
    public void before()
    {
        this.parser = MongoParser.createParser(sql);
    }

    @Test
    public void testSelect()
    {
        assertEquals("users", parser.getCollection());
        assertEquals("{\"$and\": [{\"age\": {\"$gt\": 18}}, {\"city\": \"Beijing\"}]}", parser.getQuery().toJson());
        assertEquals("{\"age\": -1}", parser.getSort().toJson());
        assertEquals(10, parser.getLimit());
    }
}
