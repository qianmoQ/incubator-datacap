package io.edurt.datacap.condor.metadata;

import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RowDefinition
        implements Serializable
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
