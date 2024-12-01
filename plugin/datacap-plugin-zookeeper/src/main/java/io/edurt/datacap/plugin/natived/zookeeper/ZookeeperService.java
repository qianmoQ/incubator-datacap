package io.edurt.datacap.plugin.natived.zookeeper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "IL_INFINITE_RECURSIVE_LOOP"})
public class ZookeeperService
        implements PluginService
{
    private ZookeeperConnection connection;
    private Response response;

    @Override
    public String validator()
    {
        return "SHOW PATHS FROM controller";
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.connection = new ZookeeperConnection(configure, this.response);
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
            log.info("Execute zookeeper plugin logic started");
            this.response = this.connection.getResponse();
            Adapter processor = new ZookeeperAdapter(this.connection, new ZookeeperParser(content));
            this.response = processor.handlerExecute(content);
            log.info("Execute zookeeper plugin logic end");
        }
        return this.response;
    }

    @Override
    public Response execute(Configure configure, String content)
    {
        this.connect(configure);
        return execute(configure, content);
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.connection.destroy();
        }
    }
}
