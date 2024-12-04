package io.edurt.datacap.sql.node;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Expression
{
    private ExpressionType type;
    private Object value;
    private List<Expression> children;

    public enum ExpressionType
    {
        LITERAL, COLUMN_REFERENCE, FUNCTION_CALL, BINARY_OP, UNARY_OP, FUNCTION
    }
}
