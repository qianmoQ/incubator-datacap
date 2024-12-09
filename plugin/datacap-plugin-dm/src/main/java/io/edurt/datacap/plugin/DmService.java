package io.edurt.datacap.plugin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import io.edurt.datacap.spi.model.Configure;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class DmService
        implements PluginService
{
    @Override
    public String validator()
    {
        return "SELECT DB_VERSION AS version\n" +
                "FROM V$INSTANCE";
    }

    @Override
    public String driver()
    {
        return "dm.jdbc.driver.DmDriver";
    }

    @Override
    public String connectType()
    {
        return "dm";
    }

    @Override
    public String url(Configure configure)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("jdbc:");
        buffer.append(configure.getType());
        buffer.append("://");
        buffer.append(configure.getHost());
        buffer.append(":");
        buffer.append(configure.getPort());
        if (configure.getDatabase().isPresent()) {
            buffer.append("?SCHEMA=");
            buffer.append(configure.getDatabase().get());
        }
        if (configure.getSsl().isPresent()) {
            if (configure.getDatabase().isEmpty()) {
                buffer.append(String.format("?ssl=%s", configure.getSsl().get()));
            }
            else {
                buffer.append(String.format("&ssl=%s", configure.getSsl().get()));
            }
        }
        if (configure.getEnv().isPresent()) {
            Map<String, Object> env = configure.getEnv().get();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            if (configure.getDatabase().isEmpty()) {
                buffer.append("?");
            }
            else {
                if (configure.getIsAppendChar()) {
                    buffer.append("&");
                }
            }
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }
}
