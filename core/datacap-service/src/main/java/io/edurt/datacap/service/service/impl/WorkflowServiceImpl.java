package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Sets;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.executor.ExecutorService;
import io.edurt.datacap.executor.common.RunEngine;
import io.edurt.datacap.executor.common.RunMode;
import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.executor.common.RunWay;
import io.edurt.datacap.executor.configure.ExecutorConfigure;
import io.edurt.datacap.executor.configure.ExecutorRequest;
import io.edurt.datacap.executor.configure.ExecutorResponse;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.service.common.FolderUtils;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.entity.WorkflowEntity;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.itransient.configuration.NodeConfiguration;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class WorkflowServiceImpl
        implements WorkflowService
{
    private final InitializerConfigure initializer;
    private final PluginManager pluginManager;
    private final Environment environment;

    public WorkflowServiceImpl(InitializerConfigure initializer, PluginManager pluginManager, Environment environment)
    {
        this.initializer = initializer;
        this.pluginManager = pluginManager;
        this.environment = environment;
    }

    @Override
    public CommonResponse<WorkflowEntity> saveOrUpdate(BaseRepository<WorkflowEntity, Long> repository, WorkflowEntity configure)
    {
        log.info("Starting workflow save/update process for executor: {}, code: {}",
                configure.getExecutor(), configure.getCode());

        UserEntity user = UserDetailsService.getUser();
        String date = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
        String workHome = FolderUtils.getWorkHome(
                initializer.getDataHome(),
                user.getUsername(),
                String.join(File.separator, "workflow", configure.getExecutor().toLowerCase(), date)
        );
        log.debug("Created work directory: {}", workHome);

        configure.setState(RunState.CREATED);
        configure.setUser(user);
        configure.setWork(workHome);
        configure.setFrom(null);
        configure.setTo(null);
        WorkflowEntity saved = repository.save(configure);
        log.info("Workflow initialized with state CREATED, code: {}", saved.getCode());

        configure.setState(RunState.RUNNING);
        saved = repository.save(configure);
        log.info("Workflow updated to RUNNING state, code: {}", saved.getCode());

        pluginManager.getPlugin(configure.getExecutor())
                .ifPresentOrElse(
                        plugin -> {
                            log.info("Found plugin for executor: {}", configure.getExecutor());
                            final java.util.concurrent.ExecutorService executorService = Executors.newSingleThreadExecutor();
                            try {
                                Files.createDirectories(Path.of(configure.getWork()));
                                log.debug("Created work directories at: {}", configure.getWork());

                                Optional<NodeConfiguration> inputNode = configure.getConfigure()
                                        .getNodes()
                                        .stream()
                                        .filter(v -> v.getCategory().equalsIgnoreCase("source"))
                                        .findFirst();
                                checkArgument(inputNode.isPresent(), "Source node must not be null");
                                log.debug("Found source node: {}", inputNode.get().getKey());

                                Optional<NodeConfiguration> outputNode = configure.getConfigure()
                                        .getNodes()
                                        .stream()
                                        .filter(v -> v.getCategory().equalsIgnoreCase("sink"))
                                        .findFirst();
                                checkArgument(outputNode.isPresent(), "Sink node must not be null");
                                log.debug("Found sink node: {}", outputNode.get().getKey());

                                ExecutorConfigure form = new ExecutorConfigure(
                                        inputNode.get().getKey(),
                                        MapUtils.toProperties(inputNode.get().getData()),
                                        Sets.newHashSet()
                                );

                                ExecutorConfigure to = new ExecutorConfigure(
                                        outputNode.get().getKey(),
                                        MapUtils.toProperties(outputNode.get().getData()),
                                        Sets.newHashSet()
                                );

                                String executorHome = environment.getProperty(
                                        String.format("datacap.executor.%s.home",
                                                configure.getExecutor().toLowerCase())
                                );
                                log.debug("Executor home directory: {}", executorHome);

                                ExecutorRequest request = new ExecutorRequest(
                                        configure.getWork(),
                                        executorHome,
                                        configure.getCode(),
                                        user.getUsername(),
                                        form,
                                        to,
                                        RunMode.valueOf(requireNonNull(environment.getProperty("datacap.executor.mode"))),
                                        RunWay.valueOf(requireNonNull(environment.getProperty("datacap.executor.way"))),
                                        environment.getProperty("datacap.executor.startScript"),
                                        RunEngine.valueOf(requireNonNull(environment.getProperty("datacap.executor.engine")))
                                );
                                log.info("Created executor request for workflow: {}", configure.getCode());

                                executorService.submit(() -> {
                                    try {
                                        log.info("Starting async execution for workflow: {}", configure.getCode());
                                        ExecutorService pluginService = plugin.getService(ExecutorService.class);

                                        repository.findByCode(configure.getCode())
                                                .ifPresent(finalConfigure -> {
                                                    log.debug("Found workflow by code: {}", configure.getCode());
                                                    initializer.getTaskExecutors().put(finalConfigure.getCode(), executorService);

                                                    log.info("Executing workflow: {}", finalConfigure.getCode());
                                                    ExecutorResponse response = pluginService.start(request);
                                                    log.info("Execution completed with state: {}", response.getState());

                                                    finalConfigure.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                                                    finalConfigure.setState(response.getState());
                                                    finalConfigure.setMessage(response.getMessage());
                                                    finalConfigure.setElapsed(
                                                            finalConfigure.getUpdateTime().getTime() -
                                                                    finalConfigure.getCreateTime().getTime()
                                                    );

                                                    WorkflowEntity updated = repository.save(finalConfigure);
                                                    log.info("Workflow updated with final state: {}, elapsed time: {} ms",
                                                            updated.getState(), updated.getElapsed());
                                                });
                                    }
                                    catch (Exception e) {
                                        log.error("Error during workflow execution: {}", configure.getCode(), e);
                                    }
                                    finally {
                                        log.info("Cleaning up resources for workflow: {}", configure.getCode());
                                        initializer.getTaskExecutors().remove(configure.getCode());
                                        executorService.shutdownNow();
                                    }
                                });
                            }
                            catch (IOException e) {
                                log.error("Failed to create work directory for workflow: {}", configure.getCode(), e);
                                configure.setState(RunState.FAILURE);
                                configure.setMessage(ExceptionUtils.getStackTrace(e));
                                repository.save(configure);
                                throw new RuntimeException(e);
                            }
                        },
                        () -> {
                            String errorMsg = String.format("Executor [ %s ] not found", configure.getExecutor());
                            log.error(errorMsg);
                            configure.setState(RunState.FAILURE);
                            configure.setMessage(errorMsg);
                            repository.save(configure);
                            throw new RuntimeException(errorMsg);
                        }
                );

        log.info("Workflow process initiated successfully for: {}", configure.getCode());
        return CommonResponse.success(configure);
    }
}
