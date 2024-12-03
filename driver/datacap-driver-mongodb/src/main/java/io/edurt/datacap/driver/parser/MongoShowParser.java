package io.edurt.datacap.driver.parser;

import io.edurt.datacap.sql.statement.ShowStatement;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoShowParser
        extends MongoParser
{
    public MongoShowParser(ShowStatement statement)
    {
        parseShowStatement(statement);
    }

    public void parseShowStatement(ShowStatement show)
    {
        this.showType = show.getShowType();
        switch (show.getShowType()) {
            case DATABASES:
                this.command = "listDatabases";
                if (show.getPattern() != null) {
                    // Convert SQL LIKE pattern to MongoDB regex pattern
                    String pattern = convertLikeToRegex(show.getPattern());
                    this.filter = new Document("name", new Document("$regex", pattern));
                }
                break;

            case TABLES:
                this.command = "listCollections";
                if (show.getDatabaseName() != null) {
                    this.database = show.getDatabaseName();
                }
                if (show.getPattern() != null) {
                    String pattern = convertLikeToRegex(show.getPattern());
                    this.filter = new Document("name", new Document("$regex", pattern));
                }
                break;

            case COLUMNS:
                this.command = "listFields";
                if (show.getDatabaseName() != null) {
                    this.database = show.getDatabaseName();
                }
                if (show.getTableName() != null) {
                    this.collection = show.getTableName();
                }
                if (show.getPattern() != null) {
                    String pattern = convertLikeToRegex(show.getPattern());
                    this.filter = new Document("name", new Document("$regex", pattern));
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported SHOW type: " + show.getShowType());
        }
    }

    private String convertLikeToRegex(String likePattern)
    {
        // Remove quotes if present
        if (likePattern.startsWith("'") && likePattern.endsWith("'")) {
            likePattern = likePattern.substring(1, likePattern.length() - 1);
        }
        else if (likePattern.startsWith("\"") && likePattern.endsWith("\"")) {
            likePattern = likePattern.substring(1, likePattern.length() - 1);
        }

        // Convert SQL LIKE pattern to MongoDB regex pattern
        return likePattern
                .replace("%", ".*")      // % matches any sequence of characters
                .replace("_", ".");      // _ matches any single character
    }
}
