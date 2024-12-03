package io.edurt.datacap.sql.statement;

import io.edurt.datacap.sql.node.Expression;
import io.edurt.datacap.sql.node.clause.LimitClause;
import io.edurt.datacap.sql.node.element.OrderByElement;
import io.edurt.datacap.sql.node.element.SelectElement;
import io.edurt.datacap.sql.node.element.TableElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectStatement
        extends SQLStatement
{
    private List<SelectElement> selectElements;
    private List<TableElement> fromSources;
    private Expression whereClause;
    private List<Expression> groupByElements;
    private Expression havingClause;
    private List<OrderByElement> orderByElements;
    private LimitClause limitClause;

    public SelectStatement()
    {
        super(StatementType.SELECT);
    }
}
