package io.edurt.datacap.spi.connection;

import io.edurt.datacap.plugin.PluginContextManager;
import io.edurt.datacap.plugin.loader.PluginClassLoader;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
public class JdbcConnection
        extends io.edurt.datacap.spi.connection.Connection
{
    private java.sql.Connection jdbcConnection;

    public JdbcConnection(JdbcConfigure configure, Response response)
    {
        super(configure, response);
    }

    protected String formatJdbcUrl()
    {
        JdbcConfigure jdbcConfigure = getJdbcConfigure();
        StringBuilder buffer = new StringBuilder();
        buffer.append("jdbc:");
        buffer.append(jdbcConfigure.getJdbcType());
        if (jdbcConfigure.getJdbcType().equals("influxdb")) {
            buffer.append(":");
        }
        else {
            buffer.append("://");
        }
        buffer.append(jdbcConfigure.getHost());
        buffer.append(":");
        buffer.append(jdbcConfigure.getPort());
        if (jdbcConfigure.getDatabase().isPresent()) {
            if (jdbcConfigure.getJdbcType().equals("solr")) {
                buffer.append("/?collection=");
            }
            else {
                buffer.append("/");
            }
            buffer.append(jdbcConfigure.getDatabase().get());
        }
        if (jdbcConfigure.getSsl().isPresent()) {
            buffer.append(String.format("?ssl=%s", jdbcConfigure.getSsl().get()));
        }
        if (jdbcConfigure.getEnv().isPresent()) {
            Map<String, Object> env = jdbcConfigure.getEnv().get();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            if (jdbcConfigure.getSsl().isEmpty()) {
                buffer.append("?");
            }
            else {
                if (jdbcConfigure.getIsAppendChar()) {
                    buffer.append("&");
                }
            }
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }

    protected JdbcConfigure getJdbcConfigure()
    {
        return (JdbcConfigure) this.configure;
    }

    protected java.sql.Connection openConnection()
    {
        JdbcConfigure jdbcConfigure = getJdbcConfigure();

        try {
            PluginClassLoader pluginClassLoader = jdbcConfigure.getPlugin().getPluginClassLoader();
            PluginContextManager.runWithClassLoader(pluginClassLoader, () -> {
                Class<?> driverClass = Class.forName(jdbcConfigure.getJdbcDriver(), true, pluginClassLoader);
                Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(new DriverShim(driver));

                String url = formatJdbcUrl();
                log.info("Connection driver {}", jdbcConfigure.getJdbcDriver());
                log.info("Connection url {}", url);
                if (jdbcConfigure.getUsername().isPresent() && jdbcConfigure.getPassword().isPresent()) {
                    log.info("Connection username with {} password with {}",
                            jdbcConfigure.getUsername().get(), "***");
                    this.jdbcConnection = DriverManager.getConnection(url,
                            jdbcConfigure.getUsername().get(),
                            jdbcConfigure.getPassword().get());
                }
                else {
                    log.info("Connection username and password not present");
                    Properties properties = new Properties();
                    if (jdbcConfigure.getUsername().isPresent()) {
                        properties.put("user", jdbcConfigure.getUsername().get());
                    }
                    this.jdbcConnection = DriverManager.getConnection(url, properties);
                }
                response.setIsConnected(Boolean.TRUE);

                return null;
            });
        }
        catch (Exception ex) {
            log.error("Connection failed ", ex);
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return this.jdbcConnection;
    }

    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.jdbcConnection)) {
            try {
                this.jdbcConnection.close();
            }
            catch (SQLException ex) {
                log.error("Connection close failed ", ex);
            }
        }
    }

    private static class DriverShim
            implements Driver
    {
        private final Driver driver;

        DriverShim(Driver d)
        {
            this.driver = d;
        }

        public Connection connect(String url, Properties info)
                throws SQLException
        {
            return driver.connect(url, info);
        }

        public boolean acceptsURL(String url)
                throws SQLException
        {
            return driver.acceptsURL(url);
        }

        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
                throws SQLException
        {
            return driver.getPropertyInfo(url, info);
        }

        public int getMajorVersion()
        {
            return driver.getMajorVersion();
        }

        public int getMinorVersion()
        {
            return driver.getMinorVersion();
        }

        public boolean jdbcCompliant()
        {
            return driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger()
        {
            return null;
        }
    }
}
