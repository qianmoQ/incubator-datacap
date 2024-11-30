package io.edurt.datacap.plugin.natived.alioss;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class AliOssService
        implements PluginService
{
    private AliossConnection connection;
    private Response response;

    @Override
    public String validator()
    {
        return "SELECT * FROM default";
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.connection = new AliossConnection(configure, this.response);
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
            log.info("Execute alioss plugin logic started");
            this.response = this.connection.getResponse();
            Adapter processor = new AliossAdapter(this.connection, new AliossParser(content));
            this.response = processor.handlerExecute(content);
            log.info("Execute alioss plugin logic end");
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
