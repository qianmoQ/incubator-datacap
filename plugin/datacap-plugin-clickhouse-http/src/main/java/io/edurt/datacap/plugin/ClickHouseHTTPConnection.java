package io.edurt.datacap.plugin;

import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.model.Response;

public class ClickHouseHTTPConnection
        extends HttpConnection
{
    public ClickHouseHTTPConnection(HttpConfigure httpConfigure, Response response)
    {
        super(httpConfigure, response);
    }
}
