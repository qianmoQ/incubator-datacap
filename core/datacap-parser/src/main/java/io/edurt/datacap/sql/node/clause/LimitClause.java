package io.edurt.datacap.sql.node.clause;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LimitClause
{
    private long limit;
    private long offset;
}
