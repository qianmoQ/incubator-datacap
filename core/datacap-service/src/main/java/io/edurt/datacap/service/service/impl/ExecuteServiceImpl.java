package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.sql.SqlBuilder;
import io.edurt.datacap.common.sql.configure.SqlBody;
import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.service.audit.AuditPlugin;
import io.edurt.datacap.service.body.ExecuteDslBody;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.service.ExecuteService;
import io.edurt.datacap.spi.PluginService;
import io.edurt.datacap.spi.model.Configure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Slf4j
@Service
public class ExecuteServiceImpl
        implements ExecuteService
{
    private final SourceRepository sourceRepository;
    private final Environment environment;
    private final PluginManager pluginManager;

    public ExecuteServiceImpl(SourceRepository sourceRepository, Environment environment, PluginManager pluginManager)
    {
        this.sourceRepository = sourceRepository;
        this.environment = environment;
        this.pluginManager = pluginManager;
    }

    @AuditPlugin
    @Override
    public CommonResponse<Object> execute(ExecuteEntity configure)
    {
        return this.sourceRepository.findById(Long.valueOf(configure.getName()))
                .map(entity -> {
                    return pluginManager.getPlugin(entity.getType())
                            .map(plugin -> {
                                PluginService service = plugin.getService(PluginService.class);
                                Configure _configure = Configure.builder()
                                        .host(entity.getHost())
                                        .port(entity.getPort())
                                        .username(Optional.ofNullable(entity.getUsername()))
                                        .password(Optional.ofNullable(entity.getPassword()))
                                        .database(StringUtils.isNotEmpty(entity.getDatabase()) ? Optional.ofNullable(entity.getDatabase()) : Optional.empty())
                                        .ssl(Optional.ofNullable(entity.getSsl()))
                                        .env(Optional.ofNullable(entity.getConfigures()))
                                        .format(configure.getFormat())
                                        .usedConfig(entity.isUsedConfig())
                                        .pluginManager(pluginManager)
                                        .classLoader(plugin.getClassLoader())
                                        .build();
                                if (entity.isUsedConfig()) {
                                    _configure.setUsername(Optional.of(entity.getUser().getUsername()));
                                    String configHome = environment.getProperty("datacap.config.data");
                                    if (StringUtils.isEmpty(configHome)) {
                                        configHome = String.join(File.separator, System.getProperty("user.dir"), "config");
                                    }
                                    _configure.setHome(configHome);
                                    _configure.setId(String.valueOf(entity.getId()));
                                }
//                                if (configure.getMode().equals(QueryMode.ADHOC)) {
//                                    try {
//                                        if (initializerConfigure.getAutoLimit()) {
//                                            Optional<SqlParser> parserOptional = this.injector.getInstance(Key.get(new TypeLiteral<Set<SqlParser>>() {}))
//                                                    .stream()
//                                                    .filter(parser -> parser.name().equalsIgnoreCase(plugin.name()))
//                                                    .findFirst();
//                                            ParserResponse response = parserOptional.orElse(injector.getInstance(Key.get(new TypeLiteral<Set<SqlParser>>() {}))
//                                                            .stream()
//                                                            .filter(parser -> parser.name().equalsIgnoreCase(initializerConfigure.getSqlParserDefaultEngine())).findFirst().get())
//                                                    .parse(configure.getContent());
//
//                                            if (response.isParser() && response.getType().equals(StatementType.SELECT) && response.getTable().getLimit() == null) {
//                                                configure.setContent(String.format("%s%nLIMIT %s", configure.getContent(), configure.getLimit()));
//                                            }
//                                        }
//                                    }
//                                    catch (Exception exception) {
//                                        log.warn("Ignore auto limit", exception);
//                                    }
//                                }
                                io.edurt.datacap.spi.model.Response response = service.execute(_configure, configure.getContent());
                                response.setContent(configure.getContent());
                                if (response.getIsSuccessful()) {
                                    return CommonResponse.success(response);
                                }
                                return CommonResponse.failure(ServiceState.PLUGIN_EXECUTE_FAILED, response.getMessage());
                            })
                            .orElseGet(() -> CommonResponse.failure(ServiceState.PLUGIN_NOT_FOUND));
                })
                .orElse(CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND));
    }

    @Override
    public CommonResponse<Object> execute(ExecuteDslBody configure)
    {
        ExecuteEntity executeEntity = new ExecuteEntity();
        executeEntity.setEnv(configure.getEnv());
        executeEntity.setName(configure.getName());
        executeEntity.setFormat(configure.getFormat());
        SqlBody body = configure.getConfigure();
        body.setType(SqlType.SELECT);
        SqlBuilder builder = new SqlBuilder(body);
        executeEntity.setContent(builder.getSql());
        return this.execute(executeEntity);
    }
}
