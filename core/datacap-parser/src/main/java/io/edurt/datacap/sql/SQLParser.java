package io.edurt.datacap.sql;

import io.edurt.datacap.sql.parser.SqlBaseLexer;
import io.edurt.datacap.sql.parser.SqlBaseParser;
import io.edurt.datacap.sql.statement.SQLStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class SQLParser
{
    public static SQLStatement parse(String sql)
    {
        try {
            // 创建词法分析器和语法分析器
            // Create lexer and parser instance
            SqlBaseLexer lexer = new SqlBaseLexer(CharStreams.fromString(sql));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SqlBaseParser parser = new SqlBaseParser(tokens);

            // 使用自定义错误监听器
            // Use custom error listener
            parser.removeErrorListeners();
            parser.addErrorListener(new SQLParserErrorListener());

            // 获取解析树
            // Get parse tree
            ParseTree tree = parser.singleStatement();

            // 访问解析树
            // Visit parse tree
            SQLVisitor visitor = new SQLVisitor();
            return visitor.visit(tree);
        }
        catch (Exception e) {
            throw new SQLParseException("Failed to parse SQL: " + e.getMessage(), e);
        }
    }
}
