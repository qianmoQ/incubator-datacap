package io.edurt.datacap.condor;

public class SQLResult
{
    private final boolean success;
    private final String message;
    private Object data;

    public SQLResult(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public SQLResult(boolean success, String message, Object data)
    {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getMessage()
    {
        return message;
    }

    public Object getData()
    {
        return data;
    }
}
