package io.edurt.datacap.condor.metadata;

import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RowDefinition
{
    @Setter
    private Map<String, Object> values;

    public RowDefinition()
    {
        this.values = new ConcurrentHashMap<>();
    }

    public void setValue(String columnName, Object value)
    {
        values.put(columnName, value);
    }

    public Object getValue(String columnName)
    {
        return values.get(columnName);
    }
}
