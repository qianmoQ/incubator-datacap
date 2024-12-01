package io.edurt.datacap.plugin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class RedisJdbcService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "redis";
    }

    @Override
    public String driver()
    {
        return "io.edurt.datacap.driver.redis.RedisDriver";
    }
}
