package io.edurt.datacap.service.itransient;

import io.edurt.datacap.common.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlConfigure
{
    private String column;
    private Type type;
    private String expression;
}
