package io.edurt.datacap.sql.node.clause;

import io.edurt.datacap.sql.node.Expression;
import io.edurt.datacap.sql.node.element.TableElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinClause
{
    private JoinType joinType;
    private TableElement rightTable;
    private Expression condition;

    public enum JoinType
    {
        INNER, LEFT, RIGHT, FULL, CROSS, NATURAL
    }
}
