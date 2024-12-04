package io.edurt.datacap.plugin.natived.alioss;

import io.edurt.datacap.spi.parser.SqlParser;
import io.edurt.datacap.sql.statement.SQLStatement;
import io.edurt.datacap.sql.statement.SelectStatement;
import io.edurt.datacap.sql.statement.ShowStatement;

public class AliossParser
        extends SqlParser
{
    public AliossParser(String content)
    {
        super(content);
    }

    @Override
    public String getExecuteContext()
    {
        SQLStatement statement = this.getStatement();

        if (statement instanceof SelectStatement) {
            SelectStatement selectStatement = (SelectStatement) statement;
            return selectStatement.getFromSources().get(0).getTableName();
        }
        else if (statement instanceof ShowStatement) {
            ShowStatement showStatement = (ShowStatement) statement;
            return showStatement.getTableName();
        }
        else {
            throw new RuntimeException("Unsupported statement: " + statement);
        }
    }
}
