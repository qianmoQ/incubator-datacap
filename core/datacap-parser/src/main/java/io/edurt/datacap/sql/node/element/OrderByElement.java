package io.edurt.datacap.sql.node.element;

import io.edurt.datacap.sql.node.Expression;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderByElement
{
    private Expression expression;
    private boolean ascending;
}
