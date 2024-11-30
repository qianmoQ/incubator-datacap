package io.edurt.datacap.plugin.jdbc.cratedb;

import io.edurt.datacap.spi.PluginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrateDBService
        implements PluginService
{
    @Override
    public String connectType()
    {
        return "crate";
    }

    @Override
    public String driver()
    {
        return "io.crate.client.jdbc.CrateDriver";
    }
}
