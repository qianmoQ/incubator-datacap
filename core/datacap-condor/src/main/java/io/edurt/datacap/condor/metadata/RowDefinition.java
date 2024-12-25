package io.edurt.datacap.condor.metadata;

import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RowDefinition
        implements Serializable
{
    private static final long serialVersionUID = 3124538617601738211L;

    @Setter
    private Map<String, Object> values;

    public RowDefinition()
    {
        this.values = new HashMap<>();
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
