package io.edurt.datacap.plugin.jdbc.clickhouse;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class ClickHouseService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "clickhouse";
    }

    @Override
    public String driver()
    {
        return "com.clickhouse.jdbc.ClickHouseDriver";
    }
}
