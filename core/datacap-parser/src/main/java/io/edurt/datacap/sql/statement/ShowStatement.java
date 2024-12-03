package io.edurt.datacap.sql.statement;

import io.edurt.datacap.sql.node.Expression;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowStatement
        extends SQLStatement
{
    private ShowType showType;
    private String databaseName;
    private String tableName;
    private String pattern;
    private Expression whereCondition;

    public ShowStatement()
    {
        super(StatementType.SHOW);
    }

    public enum ShowType
    {
        DATABASES,
        TABLES,
        COLUMNS
    }
}
