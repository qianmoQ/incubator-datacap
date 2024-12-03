package io.edurt.datacap.driver;

import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class MongoQueryParser
{
    // Getters for parsed components
    // 获取解析后的组件
    private String collection;
    private Document query;
    private Document update;
    private List<String> fields;
    private Document sort;
    private int limit = -1;
    private int skip = -1;

    // SQL keywords pattern
    // SQL关键字匹配模式
    private static final Pattern SELECT_PATTERN =
            Pattern.compile("SELECT\\s+(.+?)\\s+FROM\\s+(\\w+)\\s*(?:WHERE\\s+(.+?))?\\s*(?:ORDER\\s+BY\\s+(.+?))?\\s*(?:LIMIT\\s+(\\d+))?\\s*(?:OFFSET\\s+(\\d+))?\\s*$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern UPDATE_PATTERN =
            Pattern.compile("UPDATE\\s+(\\w+)\\s+SET\\s+(.+?)(?:\\s+WHERE\\s+(.+))?",
                    Pattern.CASE_INSENSITIVE);

    // Constructor that parses SQL query
    // 构造函数，解析SQL查询
    public MongoQueryParser(String sql)
    {
        parseSql(sql);
    }

    // Parse SQL statement
    // 解析SQL语句
    private void parseSql(String sql)
    {
        sql = sql.trim();

        if (sql.toUpperCase().startsWith("SELECT")) {
            parseSelectStatement(sql);
        }
        else if (sql.toUpperCase().startsWith("UPDATE")) {
            parseUpdateStatement(sql);
        }
        else {
            throw new IllegalArgumentException("Unsupported SQL operation: " + sql);
        }
    }

    // Parse SELECT statement
    // 解析SELECT语句
    private void parseSelectStatement(String sql)
    {
        Matcher matcher = SELECT_PATTERN.matcher(sql);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid SELECT statement: " + sql);
        }

        // Parse fields
        // 解析字段
        String fieldsList = matcher.group(1);
        this.fields = new ArrayList<>();
        if (!fieldsList.equals("*")) {
            Arrays.stream(fieldsList.split(","))
                    .map(String::trim)
                    .forEach(fields::add);
        }

        // Get collection name
        // 获取集合名称
        this.collection = matcher.group(2);

        // Parse WHERE clause
        // 解析WHERE子句
        String whereClause = matcher.group(3);
        this.query = whereClause != null ? parseWhereClause(whereClause) : new Document();

        // Parse ORDER BY
        // 解析ORDER BY
        String orderBy = matcher.group(4);
        if (orderBy != null) {
            this.sort = parseOrderBy(orderBy);
        }

        // Parse LIMIT and OFFSET
        // 解析LIMIT和OFFSET
        String limitStr = matcher.group(5);
        if (limitStr != null) {
            this.limit = Integer.parseInt(limitStr);
        }

        String offsetStr = matcher.group(6);
        if (offsetStr != null) {
            this.skip = Integer.parseInt(offsetStr);
        }
    }

    // Parse UPDATE statement
    // 解析UPDATE语句
    private void parseUpdateStatement(String sql)
    {
        Matcher matcher = UPDATE_PATTERN.matcher(sql);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid UPDATE statement: " + sql);
        }

        // Get collection name
        // 获取集合名称
        this.collection = matcher.group(1);

        // Parse SET clause
        // 解析SET子句
        String setClause = matcher.group(2);
        this.update = parseSetClause(setClause);

        // Parse WHERE clause
        // 解析WHERE子句
        String whereClause = matcher.group(3);
        this.query = whereClause != null ? parseWhereClause(whereClause) : new Document();
    }

    // Parse WHERE clause to MongoDB query
    // 解析WHERE子句转换为MongoDB查询
    private Document parseWhereClause(String whereClause)
    {
        Document query = new Document();
        String[] conditions = whereClause.split("AND");

        for (String condition : conditions) {
            condition = condition.trim();

            // Handle different operators
            // 处理不同的操作符
            if (condition.contains("=")) {
                String[] parts = condition.split("=");
                query.put(parts[0].trim(), parseValue(parts[1].trim()));
            }
            else if (condition.contains(">")) {
                String[] parts = condition.split(">");
                query.put(parts[0].trim(), new Document("$gt", parseValue(parts[1].trim())));
            }
            else if (condition.contains("<")) {
                String[] parts = condition.split("<");
                query.put(parts[0].trim(), new Document("$lt", parseValue(parts[1].trim())));
            }
            else if (condition.toLowerCase().contains("like")) {
                String[] parts = condition.split("LIKE", 2);
                String pattern = parts[1].trim().replaceAll("%", ".*").replaceAll("'", "");
                query.put(parts[0].trim(), Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
            }
        }

        return query;
    }

    // Parse SET clause for UPDATE statement
    // 解析UPDATE语句的SET子句
    private Document parseSetClause(String setClause)
    {
        Document updateDoc = new Document();
        Document setDoc = new Document();

        String[] setPairs = setClause.split(",");
        for (String setPair : setPairs) {
            String[] parts = setPair.trim().split("=");
            String field = parts[0].trim();
            Object value = parseValue(parts[1].trim());
            setDoc.put(field, value);
        }

        updateDoc.put("$set", setDoc);
        return updateDoc;
    }

    // Parse ORDER BY clause
    // 解析ORDER BY子句
    private Document parseOrderBy(String orderByClause)
    {
        Document orderBy = new Document();
        String[] parts = orderByClause.split(",");

        for (String part : parts) {
            part = part.trim();
            if (part.toUpperCase().endsWith("DESC")) {
                String field = part.substring(0, part.length() - 4).trim();
                orderBy.put(field, -1);
            }
            else {
                String field = part.toUpperCase().endsWith("ASC") ?
                        part.substring(0, part.length() - 3).trim() : part;
                orderBy.put(field.trim(), 1);
            }
        }

        return orderBy;
    }

    // Parse string value to appropriate type
    // 将字符串值解析为适当的类型
    private Object parseValue(String value)
    {
        value = value.trim();

        // Remove quotes if present
        // 如果有引号则移除
        if (value.startsWith("'") && value.endsWith("'")) {
            return value.substring(1, value.length() - 1);
        }

        // Try parsing as number
        // 尝试解析为数字
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            }
            else {
                return Long.parseLong(value);
            }
        }
        catch (NumberFormatException e) {
            // Return as string if not a number
            // 如果不是数字则返回字符串
            return value;
        }
    }
}
