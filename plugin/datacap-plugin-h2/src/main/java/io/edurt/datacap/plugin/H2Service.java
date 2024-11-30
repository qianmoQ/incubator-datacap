package io.edurt.datacap.plugin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.PluginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class H2Service
        implements PluginService
{
    @Override
    public String validator()
    {
        return "SELECT SETTING_VALUE AS version\n" +
                "FROM INFORMATION_SCHEMA.SETTINGS\n" +
                "WHERE SETTING_NAME  = 'info.VERSION'";
    }

    @Override
    public String connectType()
    {
        return "h2:mem";
    }

    @Override
    public String driver()
    {
        return "org.h2.Driver";
    }
}
