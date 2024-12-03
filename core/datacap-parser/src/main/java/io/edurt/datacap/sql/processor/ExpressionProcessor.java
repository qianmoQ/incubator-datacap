package io.edurt.datacap.sql.processor;

import io.edurt.datacap.sql.node.Expression;
import io.edurt.datacap.sql.parser.SqlBaseBaseVisitor;
import io.edurt.datacap.sql.parser.SqlBaseParser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionProcessor
        extends SqlBaseBaseVisitor<Expression>
{
    @Override
    public Expression visitAndExpression(SqlBaseParser.AndExpressionContext ctx)
    {
        Expression expr = new Expression();
        expr.setType(Expression.ExpressionType.BINARY_OP);
        expr.setValue("AND");

        List<Expression> children = new ArrayList<>();
        // 左表达式
        // Left expression
        children.add(visit(ctx.expression(0)));
        // 右表达式
        // Right expression
        children.add(visit(ctx.expression(1)));
        expr.setChildren(children);

        return expr;
    }

    @Override
    public Expression visitOrExpression(SqlBaseParser.OrExpressionContext ctx)
    {
        Expression expr = new Expression();
        expr.setType(Expression.ExpressionType.BINARY_OP);
        expr.setValue("OR");

        List<Expression> children = new ArrayList<>();
        // 左表达式
        // Left expression
        children.add(visit(ctx.expression(0)));
        // 右表达式
        // Right expression
        children.add(visit(ctx.expression(1)));
        expr.setChildren(children);

        return expr;
    }

    @Override
    public Expression visitComparisonExpression(SqlBaseParser.ComparisonExpressionContext ctx)
    {
        Expression expr = new Expression();
        expr.setType(Expression.ExpressionType.BINARY_OP);
        expr.setValue(ctx.comparisonOperator().getText());

        List<Expression> children = new ArrayList<>();
        // 左表达式
        // Left expression
        children.add(visit(ctx.expression(0)));
        // 右表达式
        // Right expression
        children.add(visit(ctx.expression(1)));
        expr.setChildren(children);

        return expr;
    }

    @Override
    public Expression visitColumnReferencePrimary(SqlBaseParser.ColumnReferencePrimaryContext ctx)
    {
        Expression expr = new Expression();
        expr.setType(Expression.ExpressionType.COLUMN_REFERENCE);
        expr.setValue(ctx.columnReference().getText());
        return expr;
    }

    @Override
    public Expression visitLiteralPrimary(SqlBaseParser.LiteralPrimaryContext ctx)
    {
        Expression expr = new Expression();
        expr.setType(Expression.ExpressionType.LITERAL);
        expr.setValue(ctx.literal().getText());
        return expr;
    }

    @Override
    public Expression visitParenExpression(SqlBaseParser.ParenExpressionContext ctx)
    {
        return visit(ctx.expression());
    }
}
