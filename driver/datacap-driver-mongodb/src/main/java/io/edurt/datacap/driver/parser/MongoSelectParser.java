package io.edurt.datacap.driver.parser;

import io.edurt.datacap.sql.node.Expression;
import io.edurt.datacap.sql.node.clause.LimitClause;
import io.edurt.datacap.sql.node.element.OrderByElement;
import io.edurt.datacap.sql.node.element.SelectElement;
import io.edurt.datacap.sql.node.element.TableElement;
import io.edurt.datacap.sql.statement.SelectStatement;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
public class MongoSelectParser
        extends MongoParser
{
    public MongoSelectParser(SelectStatement statement)
    {
        parseSelectStatement(statement);
    }

    // Parse SELECT statement
    // 解析SELECT语句
    public void parseSelectStatement(SelectStatement select)
    {
        // Parse fields
        // 解析字段
        parseSelectElements(select.getSelectElements());

        // Get collection name from FROM clause
        // 从FROM子句获取集合名称
        parseFromClause(select.getFromSources());

        // Parse WHERE clause
        // 解析WHERE子句
        if (select.getWhereClause() != null) {
            Object queryResult = parseExpression(select.getWhereClause());
            if (queryResult instanceof Document) {
                this.query = (Document) queryResult;
            }
            else {
                this.query = new Document("$eq", queryResult);
            }
        }
        else {
            this.query = new Document();
        }

        // Parse ORDER BY
        // 解析ORDER BY
        if (select.getOrderByElements() != null && !select.getOrderByElements().isEmpty()) {
            this.sort = parseOrderByElements(select.getOrderByElements());
        }

        // Parse LIMIT and OFFSET
        // 解析LIMIT和OFFSET
        LimitClause limitClause = select.getLimitClause();
        if (limitClause != null) {
            this.limit = (int) limitClause.getLimit();
            this.skip = (int) limitClause.getOffset();
        }
    }

    // Parse SELECT elements to field list
    // 解析SELECT元素到字段列表
    private void parseSelectElements(List<SelectElement> elements)
    {
        this.fields = new ArrayList<>();
        if (elements != null) {
            for (SelectElement element : elements) {
                if (element.getColumn() != null) {
                    fields.add(element.getColumn());
                }
                else if (element.getExpression() != null) {
                    fields.add(parseExpression(element.getExpression()).toString());
                }
            }
        }
    }

    // Parse FROM clause to get collection name
    // 解析FROM子句获取集合名称
    private void parseFromClause(List<TableElement> fromSources)
    {
        if (fromSources != null && !fromSources.isEmpty()) {
            TableElement mainTable = fromSources.get(0);
            this.collection = mainTable.getTableName();

            // MongoDB doesn't support JOINs
            // MongoDB不支持JOIN操作
            if (mainTable.getJoins() != null && !mainTable.getJoins().isEmpty()) {
                throw new IllegalArgumentException("MongoDB does not support JOIN operations");
            }
        }
    }

    private Object parseExpression(Expression expr)
    {
        if (expr == null) {
            return null;
        }

        switch (expr.getType()) {
            case LITERAL:
                return parseValue(expr.getValue().toString());

            case COLUMN_REFERENCE:
                return expr.getValue().toString();

            case BINARY_OP:
                String operator = expr.getValue().toString();
                List<Expression> children = expr.getChildren();

                // Handle logical operators (AND, OR)
                if ("AND".equalsIgnoreCase(operator) || "OR".equalsIgnoreCase(operator)) {
                    List<Document> conditions = new ArrayList<>();
                    for (Expression child : children) {
                        Object result = parseExpression(child);
                        if (result instanceof Document) {
                            conditions.add((Document) result);
                        }
                    }
                    return new Document(operator.equalsIgnoreCase("AND") ? "$and" : "$or", conditions);
                }

                // Handle comparison operators
                if (children != null && children.size() == 2) {
                    Expression left = children.get(0);
                    Expression right = children.get(1);

                    String field = parseExpression(left).toString();
                    Object value = parseExpression(right);

                    Document condition = new Document();
                    switch (operator) {
                        case "=":
                            condition.put(field, value);
                            break;
                        case ">":
                            condition.put(field, new Document("$gt", value));
                            break;
                        case "<":
                            condition.put(field, new Document("$lt", value));
                            break;
                        case ">=":
                            condition.put(field, new Document("$gte", value));
                            break;
                        case "<=":
                            condition.put(field, new Document("$lte", value));
                            break;
                        case "!=":
                            condition.put(field, new Document("$ne", value));
                            break;
                        case "LIKE":
                            String pattern = value.toString().replace("%", ".*");
                            condition.put(field, Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
                            break;
                        case "IN":
                            condition.put(field, new Document("$in", value));
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported operator: " + operator);
                    }
                    return condition;
                }

                throw new IllegalArgumentException("Invalid binary expression structure");

            default:
                throw new IllegalArgumentException("Unsupported expression type: " + expr.getType());
        }
    }

    // Parse ORDER BY elements to MongoDB sort document
    // 解析ORDER BY元素到MongoDB排序文档
    private Document parseOrderByElements(List<OrderByElement> elements)
    {
        Document orderBy = new Document();
        for (OrderByElement element : elements) {
            String field = element.getExpression().getValue().toString();
            orderBy.put(field, element.isAscending() ? 1 : -1);
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
