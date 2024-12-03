package io.edurt.datacap.sql.node.element;

import io.edurt.datacap.sql.node.Expression;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectElement
{
    private String column;
    private String alias;
    private Expression expression;
}
