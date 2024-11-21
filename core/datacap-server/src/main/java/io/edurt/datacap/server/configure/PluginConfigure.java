package io.edurt.datacap.server.configure;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.datacap.common.utils.EnvironmentUtils;
import io.edurt.datacap.plugin.PluginConfig;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.plugin.utils.PluginPathUtils;
import io.edurt.datacap.spi.PluginLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class PluginConfigure
{
    private final Environment environment;

    public PluginConfigure(Environment environment)
    {
        this.environment = environment;
    }

    @Bean
    public PluginManager pluginManager()
    {
        EnvironmentUtils.printEnvironmentInfo();

        String root = environment.getProperty("spring.config.location");
        Path projectRoot = PluginPathUtils.findProjectRoot();
        PluginConfig config = PluginConfig.builder()
                .pluginsDir(Path.of(String.join(File.separator, root, "plugins")))
                .build();

        // 开发模式下生效
        // In development mode, it is effective
        if (EnvironmentUtils.isIdeEnvironment()) {
            config.setPluginsDir(projectRoot.resolve(Path.of(String.join("/", root, "plugins.properties"))));
        }

        PluginManager pluginManager = new PluginManager(config);
        pluginManager.start();

        return pluginManager;
    }

    @Bean
    public Injector injector()
    {
        return Guice.createInjector(new PluginLoader());
    }
}
