package io.edurt.datacap.plugin.natived.zookeeper;

import io.edurt.datacap.spi.parser.SqlParser;
import io.edurt.datacap.sql.statement.SQLStatement;
import io.edurt.datacap.sql.statement.SelectStatement;
import io.edurt.datacap.sql.statement.ShowStatement;
import org.apache.commons.lang3.StringUtils;

public class ZookeeperParser
        extends SqlParser
{
    public ZookeeperParser(String content)
    {
        super(content);
    }

    @Override
    public String getExecuteContext()
    {
        SQLStatement statement = this.getStatement();

        if (statement instanceof SelectStatement) {
            SelectStatement selectStatement = (SelectStatement) statement;
            String tableName = selectStatement.getFromSources().get(0).getTableName();

            if (StringUtils.isEmpty(tableName)) {
                return ZookeeperPathConvert.start;
            }
            return ZookeeperPathConvert.toPath(tableName);
        }
        else if (statement instanceof ShowStatement) {
            ShowStatement showStatement = (ShowStatement) statement;
            return ZookeeperPathConvert.toPath(showStatement.getTableName());
        }
        else {
            throw new RuntimeException("Unsupported statement: " + statement);
        }
    }
}
