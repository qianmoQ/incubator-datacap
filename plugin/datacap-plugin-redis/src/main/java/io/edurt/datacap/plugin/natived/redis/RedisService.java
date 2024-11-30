package io.edurt.datacap.plugin.natived.redis;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class RedisService
        implements PluginService
{
    private RedisConnection connection;
    private Response response;

    @Override
    public String validator()
    {
        return "PING Redis";
    }

    @Override
    public String name()
    {
        return "Redis";
    }

    @Override
    public String description()
    {
        return "Integrate Redis data sources";
    }

    @Override
    public PluginType type()
    {
        return PluginType.NATIVE;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.connection = new RedisConnection(configure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
        }
    }

    @Override
    public Response execute(String content)
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            log.info("Execute redis plugin logic started");
            this.response = this.connection.getResponse();
            Adapter processor = new RedisAdapter(this.connection);
            this.response = processor.handlerExecute(content);
            log.info("Execute redis plugin logic end");
        }
        return this.response;
    }

    @Override
    public Response execute(Configure configure, String content)
    {
        connect(configure);
        return execute(content);
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.connection.destroy();
        }
    }
}
