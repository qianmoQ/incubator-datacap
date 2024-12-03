package io.edurt.datacap.sql.node.element;

import io.edurt.datacap.sql.node.clause.JoinClause;
import io.edurt.datacap.sql.statement.SelectStatement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TableElement
{
    private String tableName;
    private String alias;
    private List<JoinClause> joins;
    private SelectStatement subquery;
}
