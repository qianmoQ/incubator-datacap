package io.edurt.datacap.spi.connection;

import io.edurt.datacap.plugin.PluginContextManager;
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
    private JdbcConfigure configure;
    private Response response;
    private Connection connection;

    public JdbcConnection(JdbcConfigure configure, Response response)
    {
        super(configure, response);
    }

    protected String formatJdbcUrl()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("jdbc:");
        buffer.append(this.configure.getJdbcType());
        if (configure.getJdbcType().equals("influxdb")) {
            buffer.append(":");
        }
        else {
            buffer.append("://");
        }
        buffer.append(this.configure.getHost());
        buffer.append(":");
        buffer.append(this.configure.getPort());
        if (this.configure.getDatabase().isPresent()) {
            if (configure.getJdbcType().equals("solr")) {
                buffer.append("/?collection=");
            }
            else {
                buffer.append("/");
            }
            buffer.append(this.configure.getDatabase().get());
        }
        if (this.configure.getSsl().isPresent()) {
            buffer.append(String.format("?ssl=%s", this.configure.getSsl().get()));
        }
        if (this.configure.getEnv().isPresent()) {
            Map<String, Object> env = this.configure.getEnv().get();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            if (this.configure.getSsl().isEmpty()) {
                buffer.append("?");
            }
            else {
                if (this.configure.getIsAppendChar()) {
                    buffer.append("&");
                }
            }
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }

    protected Connection openConnection()
    {
        try {
            this.configure = (JdbcConfigure) getConfigure();
            this.response = getResponse();

            // Manually loading and registering the driver
            ClassLoader pluginClassLoader = configure.getPlugin().getClass().getClassLoader();
            PluginContextManager.runWithClassLoader(pluginClassLoader, () -> {
                Class<?> driverClass = Class.forName(this.configure.getJdbcDriver(), true, pluginClassLoader);
                Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(new DriverShim(driver));

                String url = formatJdbcUrl();
                log.info("Connection driver {}", this.configure.getJdbcDriver());
                log.info("Connection url {}", url);
                if (this.configure.getUsername().isPresent() && this.configure.getPassword().isPresent()) {
                    log.info("Connection username with {} password with {}", this.configure.getUsername().get(), "***");
                    this.connection = DriverManager.getConnection(url, this.configure.getUsername().get(), this.configure.getPassword().get());
                }
                else {
                    log.info("Connection username and password not present");
                    Properties properties = new Properties();
                    if (configure.getUsername().isPresent()) {
                        properties.put("user", configure.getUsername().get());
                    }
                    this.connection = DriverManager.getConnection(url, properties);
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
        return this.connection;
    }

    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            try {
                this.connection.close();
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

        DriverShim(Driver d) {this.driver = d;}

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

        public int getMajorVersion() {return driver.getMajorVersion();}

        public int getMinorVersion() {return driver.getMinorVersion();}

        public boolean jdbcCompliant() {return driver.jdbcCompliant();}

        @Override
        public Logger getParentLogger()
        {
            return null;
        }
    }
}
