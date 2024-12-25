package io.edurt.datacap.condor.metadata;

import lombok.Getter;

import java.util.List;

@Getter
public class TableDefinition
{
    private String tableName;
    private List<ColumnDefinition> columns;

    public TableDefinition(String tableName, List<ColumnDefinition> columns)
    {
        this.tableName = tableName;
        this.columns = columns;
    }
}
