package io.edurt.datacap.sql;

import io.edurt.datacap.sql.node.Expression;
import io.edurt.datacap.sql.node.clause.JoinClause;
import io.edurt.datacap.sql.node.clause.LimitClause;
import io.edurt.datacap.sql.node.element.OrderByElement;
import io.edurt.datacap.sql.node.element.SelectElement;
import io.edurt.datacap.sql.node.element.TableElement;
import io.edurt.datacap.sql.parser.SqlBaseBaseVisitor;
import io.edurt.datacap.sql.parser.SqlBaseParser;
import io.edurt.datacap.sql.processor.ExpressionProcessor;
import io.edurt.datacap.sql.processor.ShowProcessor;
import io.edurt.datacap.sql.statement.SQLStatement;
import io.edurt.datacap.sql.statement.SelectStatement;

import java.util.ArrayList;
import java.util.List;

public class SQLVisitor
        extends SqlBaseBaseVisitor<SQLStatement>
{
    @Override
    public SQLStatement visitSingleStatement(SqlBaseParser.SingleStatementContext ctx)
    {
        return visit(ctx.statement(0));
    }

    @Override
    public SQLStatement visitStatement(SqlBaseParser.StatementContext ctx)
    {
        if (ctx.selectStatement() != null) {
            return visitSelectStatement(ctx.selectStatement());
        }
        else if (ctx.insertStatement() != null) {
            return visitInsertStatement(ctx.insertStatement());
        }
        else if (ctx.updateStatement() != null) {
            return visitUpdateStatement(ctx.updateStatement());
        }
        else if (ctx.deleteStatement() != null) {
            return visitDeleteStatement(ctx.deleteStatement());
        }
        else if (ctx.createStatement() != null) {
            return visitCreateStatement(ctx.createStatement());
        }
        else if (ctx.alterStatement() != null) {
            return visitAlterStatement(ctx.alterStatement());
        }
        else if (ctx.dropStatement() != null) {
            return visitDropStatement(ctx.dropStatement());
        }
        else if (ctx.useStatement() != null) {
            return visitUseStatement(ctx.useStatement());
        }
        else if (ctx.showStatement() != null) {
            return visitShowStatement(ctx.showStatement());
        }
        return null;
    }

    @Override
    public SQLStatement visitSelectStatement(SqlBaseParser.SelectStatementContext ctx)
    {
        SelectStatement statement = new SelectStatement();

        // Parse SELECT elements
        if (ctx.queryExpression().queryTerm().queryPrimary().querySpecification() != null) {
            SqlBaseParser.QuerySpecificationContext querySpec =
                    ctx.queryExpression().queryTerm().queryPrimary().querySpecification();

            statement.setSelectElements(processSelectElements(querySpec.selectElements()));

            // Parse FROM clause
            if (querySpec.fromClause() != null) {
                statement.setFromSources(processFromClause(querySpec.fromClause()));
            }

            // Parse WHERE clause
            if (querySpec.whereClause() != null) {
                statement.setWhereClause(processExpression(querySpec.whereClause().expression()));
            }

            // Parse GROUP BY clause
            if (querySpec.groupByClause() != null) {
                statement.setGroupByElements(visitGroupByElements(querySpec.groupByClause()));
            }

            // Parse HAVING clause
            if (querySpec.havingClause() != null) {
                statement.setHavingClause(processExpression(querySpec.havingClause().expression()));
            }
        }

        // Parse ORDER BY clause
        if (ctx.orderByClause() != null) {
            statement.setOrderByElements(visitOrderByElements(ctx.orderByClause()));
        }

        // Parse LIMIT clause
        if (ctx.limitClause() != null) {
            statement.setLimitClause(processLimitClause(ctx.limitClause()));
        }

        return statement;
    }

    @Override
    public SQLStatement visitQueryExpression(SqlBaseParser.QueryExpressionContext ctx)
    {
        return visit(ctx.queryTerm());
    }

    @Override
    public SQLStatement visitQueryTerm(SqlBaseParser.QueryTermContext ctx)
    {
        return visit(ctx.queryPrimary());
    }

    @Override
    public SQLStatement visitQueryPrimary(SqlBaseParser.QueryPrimaryContext ctx)
    {
        if (ctx.querySpecification() != null) {
            return visit(ctx.querySpecification());
        }
        else if (ctx.queryExpression() != null) {
            return visit(ctx.queryExpression());
        }
        return null;
    }

    @Override
    public SQLStatement visitInsertStatement(SqlBaseParser.InsertStatementContext ctx)
    {
        // TODO: Implement insert statement parsing
        return null;
    }

    @Override
    public SQLStatement visitUpdateStatement(SqlBaseParser.UpdateStatementContext ctx)
    {
        // TODO: Implement update statement parsing
        return null;
    }

    @Override
    public SQLStatement visitDeleteStatement(SqlBaseParser.DeleteStatementContext ctx)
    {
        // TODO: Implement delete statement parsing
        return null;
    }

    @Override
    public SQLStatement visitCreateStatement(SqlBaseParser.CreateStatementContext ctx)
    {
        // TODO: Implement create statement parsing
        return null;
    }

    @Override
    public SQLStatement visitAlterStatement(SqlBaseParser.AlterStatementContext ctx)
    {
        // TODO: Implement alter statement parsing
        return null;
    }

    @Override
    public SQLStatement visitDropStatement(SqlBaseParser.DropStatementContext ctx)
    {
        // TODO: Implement drop statement parsing
        return null;
    }

    @Override
    public SQLStatement visitUseStatement(SqlBaseParser.UseStatementContext ctx)
    {
        // TODO: Implement use statement parsing
        return null;
    }

    @Override
    public SQLStatement visitShowStatement(SqlBaseParser.ShowStatementContext ctx)
    {
        ShowProcessor processor = new ShowProcessor();
        return processor.process(ctx);
    }

    @Override
    public SQLStatement visitQuerySpecification(SqlBaseParser.QuerySpecificationContext ctx)
    {
        SelectStatement statement = new SelectStatement();
        statement.setSelectElements(processSelectElements(ctx.selectElements()));

        if (ctx.fromClause() != null) {
            statement.setFromSources(processFromClause(ctx.fromClause()));
        }

        if (ctx.whereClause() != null) {
            statement.setWhereClause(processExpression(ctx.whereClause().expression()));
        }

        if (ctx.groupByClause() != null) {
            statement.setGroupByElements(visitGroupByElements(ctx.groupByClause()));
        }

        if (ctx.havingClause() != null) {
            statement.setHavingClause(processExpression(ctx.havingClause().expression()));
        }

        return statement;
    }

    private List<SelectElement> processSelectElements(SqlBaseParser.SelectElementsContext ctx)
    {
        List<SelectElement> elements = new ArrayList<>();

        for (SqlBaseParser.SelectElementContext elementCtx : ctx.selectElement()) {
            SelectElement element = new SelectElement();

            if (elementCtx.columnName() != null) {
                // 直接指定的列名
                // Directly specified column names
                element.setColumn(elementCtx.columnName().getText());
            }
            // 处理表达式
            // Handle expression
            if (elementCtx.expression() != null) {
                Expression expr = processExpression(elementCtx.expression());
                element.setExpression(expr);

                // 处理函数调用的情况
                // Handle function call
                if (expr.getType() == Expression.ExpressionType.FUNCTION) {
                    // 尝试从函数的参数中获取列名
                    // Try to get column name from function parameters
                    if (expr.getChildren() != null && !expr.getChildren().isEmpty()) {
                        Expression columnExpr = expr.getChildren().get(0);
                        if (columnExpr.getType() == Expression.ExpressionType.COLUMN_REFERENCE) {
                            element.setColumn(columnExpr.getValue().toString());
                        }
                    }
                }
            }
            // 处理别名
            // Handle alias
            if (elementCtx.alias() != null) {
                element.setAlias(elementCtx.alias().getText());
            }

            elements.add(element);
        }

        return elements;
    }

    private List<TableElement> processFromClause(SqlBaseParser.FromClauseContext ctx)
    {
        List<TableElement> tables = new ArrayList<>();

        for (SqlBaseParser.TableSourceContext sourceCtx : ctx.tableSource()) {
            TableElement table = new TableElement();

            // 普通获取主表信息
            // Get the primary table information
            if (sourceCtx.tablePrimary() != null) {
                SqlBaseParser.TablePrimaryContext primaryCtx = sourceCtx.tablePrimary();

                // 处理子查询
                // Handle subquery
                if (primaryCtx.selectStatement() != null) {
                    // 处理子查询的别名
                    // Handle the alias of the subquery
                    if (primaryCtx.alias() != null) {
                        table.setAlias(primaryCtx.alias().getText());
                    }
                    // 可以选择存储子查询的SelectStatement
                    SelectStatement subquery = (SelectStatement) visit(primaryCtx.selectStatement());
                    table.setSubquery(subquery);
                }
                // 处理普通表
                // Handle normal tables
                else if (primaryCtx.tableName() != null) {
                    table.setTableName(primaryCtx.tableName().getText());
                    if (primaryCtx.alias() != null) {
                        table.setAlias(primaryCtx.alias().getText());
                    }
                }
            }

            if (sourceCtx.joinedTable() != null) {
                List<JoinClause> joins = new ArrayList<>();

                // 从 joinedTable 的 tablePrimary 中获取主表信息
                // Get the primary table information from the tablePrimary of joinedTable
                SqlBaseParser.TablePrimaryContext primaryCtx = sourceCtx.joinedTable().tablePrimary();
                if (primaryCtx != null && primaryCtx.tableName() != null) {
                    table.setTableName(primaryCtx.tableName().getText());
                    if (primaryCtx.alias() != null) {
                        table.setAlias(primaryCtx.alias().getText());
                    }
                }

                // 处理 joins
                // Handle joins
                for (SqlBaseParser.JoinClauseContext joinCtx : sourceCtx.joinedTable().joinClause()) {
                    JoinClause join = processJoinClause(joinCtx);
                    joins.add(join);
                }
                table.setJoins(joins);
            }

            tables.add(table);
        }

        return tables;
    }

    private JoinClause processJoinClause(SqlBaseParser.JoinClauseContext ctx)
    {
        JoinClause join = new JoinClause();

        if (ctx.joinTypeClause() != null) {
            if (ctx.joinTypeClause().INNER() != null) {
                join.setJoinType(JoinClause.JoinType.INNER);
            }
            else if (ctx.joinTypeClause().LEFT() != null) {
                join.setJoinType(JoinClause.JoinType.LEFT);
            }
            else if (ctx.joinTypeClause().RIGHT() != null) {
                join.setJoinType(JoinClause.JoinType.RIGHT);
            }
            else if (ctx.joinTypeClause().FULL() != null) {
                join.setJoinType(JoinClause.JoinType.FULL);
            }
        }

        TableElement rightTable = new TableElement();
        rightTable.setTableName(ctx.tablePrimary().tableName().getText());
        if (ctx.tablePrimary().alias() != null) {
            rightTable.setAlias(ctx.tablePrimary().alias().getText());
        }
        join.setRightTable(rightTable);

        if (ctx.joinCondition() != null) {
            if (ctx.joinCondition().ON() != null) {
                join.setCondition(processExpression(ctx.joinCondition().expression()));
            }
        }

        return join;
    }

    private Expression processExpression(SqlBaseParser.ExpressionContext ctx)
    {
        ExpressionProcessor processor = new ExpressionProcessor();
        return processor.visit(ctx);
    }

    private List<Expression> visitGroupByElements(SqlBaseParser.GroupByClauseContext ctx)
    {
        List<Expression> groupByElements = new ArrayList<>();

        for (SqlBaseParser.GroupByElementContext elementCtx : ctx.groupByElement()) {
            groupByElements.add(processExpression(elementCtx.expression()));
        }

        return groupByElements;
    }

    private List<OrderByElement> visitOrderByElements(SqlBaseParser.OrderByClauseContext ctx)
    {
        List<OrderByElement> orderByElements = new ArrayList<>();

        for (SqlBaseParser.OrderByElementContext elementCtx : ctx.orderByElement()) {
            OrderByElement element = new OrderByElement();
            element.setExpression(processExpression(elementCtx.expression()));
            element.setAscending(elementCtx.DESC() == null);
            orderByElements.add(element);
        }

        return orderByElements;
    }

    private LimitClause processLimitClause(SqlBaseParser.LimitClauseContext ctx)
    {
        LimitClause limit = new LimitClause();

        if (ctx.INTEGER_VALUE().size() > 1) {
            limit.setOffset(Long.parseLong(ctx.INTEGER_VALUE(0).getText()));
            limit.setLimit(Long.parseLong(ctx.INTEGER_VALUE(1).getText()));
        }
        else {
            limit.setLimit(Long.parseLong(ctx.INTEGER_VALUE(0).getText()));
            if (ctx.OFFSET() != null) {
                limit.setOffset(Long.parseLong(ctx.INTEGER_VALUE(1).getText()));
            }
        }

        return limit;
    }
}
