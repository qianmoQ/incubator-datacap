package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.BeanToPropertiesUtils;
import io.edurt.datacap.executor.ExecutorService;
import io.edurt.datacap.executor.common.RunEngine;
import io.edurt.datacap.executor.common.RunMode;
import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.executor.common.RunWay;
import io.edurt.datacap.executor.configure.ExecutorConfigure;
import io.edurt.datacap.executor.configure.ExecutorRequest;
import io.edurt.datacap.executor.configure.ExecutorResponse;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.service.body.PipelineBody;
import io.edurt.datacap.service.common.ConfigureUtils;
import io.edurt.datacap.service.common.FolderUtils;
import io.edurt.datacap.service.configure.FieldType;
import io.edurt.datacap.service.configure.IConfigureExecutorField;
import io.edurt.datacap.service.configure.IConfigurePipelineType;
import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.repository.PipelineRepository;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.PipelineService;
import io.edurt.datacap.spi.json.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;

@Service
@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
public class PipelineServiceImpl
        implements PipelineService
{
    private final SourceRepository sourceRepository;
    private final Environment environment;
    private final PipelineRepository repository;
    private final InitializerConfigure initializer;
    private final PluginManager pluginManager;

    public PipelineServiceImpl(SourceRepository sourceRepository, Environment environment, PipelineRepository repository, InitializerConfigure initializer, PluginManager pluginManager)
    {
        this.sourceRepository = sourceRepository;
        this.environment = environment;
        this.repository = repository;
        this.initializer = initializer;
        this.pluginManager = pluginManager;
    }

    @Override
    public CommonResponse<Object> submit(PipelineBody configure)
    {
        Optional<SourceEntity> fromSourceOptional = sourceRepository.findByCode(configure.getFrom().getCode());
        if (fromSourceOptional.isEmpty()) {
            return CommonResponse.failure(String.format("From source [ %s ] not found", configure.getFrom().getCode()));
        }

        Optional<SourceEntity> toSourceOptional = sourceRepository.findByCode(configure.getTo().getCode());
        if (toSourceOptional.isEmpty()) {
            return CommonResponse.failure(String.format("To source [ %s ] not found", configure.getTo().getCode()));
        }

        // Input source
        SourceEntity fromSource = fromSourceOptional.get();
        Properties fromOriginProperties = configure.getFrom().getConfigures();
        if (!fromOriginProperties.containsKey("context") && configure.getContent() != null) {
            fromOriginProperties.setProperty("context", configure.getContent());
        }
        else {
            configure.setContent(fromOriginProperties.getProperty("context"));
        }
        Properties fromProperties = ConfigureUtils.convertProperties(fromSource, environment,
                IConfigurePipelineType.INPUT, configure.getExecutor(), configure.getContent(), configure.getFrom());
        Set<String> fromOptions = ConfigureUtils.convertOptions(fromSource, environment, configure.getExecutor(), IConfigurePipelineType.INPUT);
        ExecutorConfigure fromField = new ExecutorConfigure(fromSource.getType(), fromProperties, fromOptions, configure.getFrom().getProtocol());

        // Output source
        SourceEntity toSource = toSourceOptional.get();
        Properties toProperties = ConfigureUtils.convertProperties(toSource, environment,
                IConfigurePipelineType.OUTPUT, configure.getExecutor(), configure.getContent(), configure.getTo());
        Set<String> toOptions = ConfigureUtils.convertOptions(toSource, environment, configure.getExecutor(), IConfigurePipelineType.OUTPUT);
        ExecutorConfigure toField = new ExecutorConfigure(toSource.getType(), toProperties, toOptions, configure.getTo().getProtocol());

        PipelineEntity pipelineEntity = new PipelineEntity();
        if (ObjectUtils.isNotEmpty(configure.getCode())) {
            pipelineEntity = this.repository.findByCode(configure.getCode()).get();
        }
        else {
            String username = UserDetailsService.getUser().getUsername();
            String pipelineHome = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
            String workHome = FolderUtils.getWorkHome(initializer.getDataHome(), username, String.join(File.separator, "pipeline", configure.getExecutor().toLowerCase(), pipelineHome));

            String pipelineName = String.join("_",
                    username,
                    configure.getExecutor().toLowerCase(),
                    "from",
                    String.valueOf(fromSource.getId()),
                    "to",
                    String.valueOf(toSource.getId()),
                    pipelineHome);
            pipelineEntity.setName(pipelineName);
            pipelineEntity.setContent(configure.getContent());
            pipelineEntity.setState(RunState.CREATED);
            pipelineEntity.setWork(workHome);
            pipelineEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            pipelineEntity.setUser(UserDetailsService.getUser());
            pipelineEntity.setFrom(fromSource);
            pipelineEntity.setFromConfigures(fromProperties);
            pipelineEntity.setTo(toSource);
            pipelineEntity.setToConfigures(toProperties);
            pipelineEntity.setFlowConfigure(JSON.toJSON(configure.getFlow()));
        }

        String pipelineName = pipelineEntity.getName();
        String work = pipelineEntity.getWork();
        if (initializer.isSubmit()) {
            log.info("Pipeline containers is full, submit to queue [ {} ]", pipelineName);
            pipelineEntity.setState(RunState.QUEUE);
            repository.save(pipelineEntity);
            if (initializer.getTaskQueue().offer(pipelineEntity)) {
                log.info("Pipeline containers is full, submit to executor [ {} ]", pipelineName);
            }
        }
        else {
            log.info("Pipeline containers is not full, submit to executor [ {} ]", pipelineName);
            pipelineEntity.setState(RunState.RUNNING);
            repository.save(pipelineEntity);
            // TODO: Refactor
            PipelineEntity _finalPipelineEntity = pipelineEntity;
            pluginManager.getPlugin(configure.getExecutor())
                    .ifPresentOrElse(
                            plugin -> {
                                try {
                                    FileUtils.forceMkdir(new File(work));
                                }
                                catch (Exception e) {
                                    log.warn("Failed to create temporary directory", e);
                                }

                                ExecutorRequest pipeline = new ExecutorRequest(work, environment.getProperty(String.format("datacap.executor.%s.home", configure.getExecutor().toLowerCase())),
                                        pipelineName, UserDetailsService.getUser().getUsername(), fromField, toField,
                                        RunMode.valueOf(environment.getProperty("datacap.executor.mode")),
                                        RunWay.valueOf(environment.getProperty("datacap.executor.way")),
                                        environment.getProperty("datacap.executor.startScript"),
                                        RunEngine.valueOf(environment.getProperty("datacap.executor.engine")),
                                        null);

                                final java.util.concurrent.ExecutorService executorService = Executors.newCachedThreadPool();
                                executorService.submit(() -> {
                                    ExecutorService pluginService = plugin.getService(ExecutorService.class);
                                    initializer.getTaskExecutors().put(pipelineName, executorService);
                                    ExecutorResponse response = pluginService.start(pipeline);
                                    log.info("Pipeline [ {} ] executed successfully", pipelineName);
                                    _finalPipelineEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                                    _finalPipelineEntity.setState(response.getState());
                                    _finalPipelineEntity.setMessage(response.getMessage());
                                    _finalPipelineEntity.setElapsed(_finalPipelineEntity.getUpdateTime().getTime() - _finalPipelineEntity.getCreateTime().getTime());
                                    repository.save(_finalPipelineEntity);
                                    initializer.getTaskExecutors()
                                            .remove(pipelineName);
                                    executorService.shutdownNow();
                                    log.info("Pipeline [ {} ] finished", pipelineName);

                                    PipelineEntity entity = initializer.getTaskQueue().poll();
                                    if (ObjectUtils.isNotEmpty(entity)) {
                                        log.info("Extract tasks from the queue [ {} ] and start execution", entity.getName());
                                        this.submit(entity.entityToBody());
                                    }
                                    else {
                                        log.warn("The queue extraction task failed. Please check whether there are tasks in the queue. The current number of queue tasks: [ {} ]", initializer.getTaskQueue().size());
                                    }
                                });
                            },
                            () -> {
                                _finalPipelineEntity.setState(RunState.FAILURE);
                                _finalPipelineEntity.setMessage(String.format("Executor [ %s ] not found", configure.getExecutor()));
                                repository.save(_finalPipelineEntity);
                                log.error("Executor [ {} ] not found", configure.getExecutor());
                                throw new RuntimeException(String.format("Executor [ %s ] not found", configure.getExecutor()));
                            }
                    );
        }
        return CommonResponse.success(pipelineEntity.getId());
    }

    @Override
    public CommonResponse<Boolean> stop(Long id)
    {
        Optional<PipelineEntity> pipelineOptional = this.repository.findById(id);
        if (!pipelineOptional.isPresent()) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] not found", id));
        }

        PipelineEntity entity = pipelineOptional.get();
        if (entity.getState().equals(RunState.STOPPED)
                || entity.getState().equals(RunState.FAILURE)
                || entity.getState().equals(RunState.SUCCESS)
                || entity.getState().equals(RunState.TIMEOUT)) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] is already stopped", entity.getName()));
        }

        java.util.concurrent.ExecutorService service = initializer.getTaskExecutors()
                .get(entity.getName());
        if (service != null) {
            service.shutdownNow();
        }
        entity.setState(RunState.STOPPED);
        entity.setMessage(null);
        this.repository.save(entity);

        // Consume queue data for execution
        if (initializer.getTaskQueue().size() > 0) {
            PipelineEntity queueEntity = initializer.getTaskQueue()
                    .poll();
            if (queueEntity != null) {
                this.submit(entity.entityToBody());
            }
        }
        return CommonResponse.success(true);
    }

    /**
     * Retrieves the log for a given pipeline ID.
     *
     * @param code the ID of the pipeline
     * @return a response containing the log lines as a list of strings
     */
    @Override
    public CommonResponse<List<String>> log(String code)
    {
        return repository.findByCode(code)
                .map(entity -> {
                    if (entity.getState().equals(RunState.QUEUE)
                            || entity.getState().equals(RunState.CREATED)) {
                        return CommonResponse.<List<String>>failure(String.format("Pipeline [ %s ] is not running", entity.getName()));
                    }

                    List<String> lines = Lists.newArrayList();
                    try (FileInputStream stream = new FileInputStream(new File(String.format("%s/%s.log", entity.getWork(), entity.getName())))) {
                        lines.addAll(IOUtils.readLines(stream, "UTF-8"));
                        return CommonResponse.success(lines);
                    }
                    catch (IOException e) {
                        log.error("Failed to read pipeline [ {} ] log ", entity.getName(), e);
                        return CommonResponse.<List<String>>failure("Failed to read log file");
                    }
                })
                .orElse(CommonResponse.failure(String.format("Pipeline [ %s ] not found", code)));
    }

    @Override
    public CommonResponse<Long> deleteById(BaseRepository repository, Long id)
    {
        Optional<PipelineEntity> pipelineOptional = this.repository.findById(id);
        if (!pipelineOptional.isPresent()) {
            return CommonResponse.failure(String.format("Pipeline [ %s ] not found", id));
        }

        PipelineEntity entity = pipelineOptional.get();
        log.info("Delete pipeline [ {} ] work home", entity.getName());
        try {
            FileUtils.deleteDirectory(new File(entity.getWork()));
        }
        catch (IOException e) {
            log.warn("Failed to delete pipeline [ {} ] work home {}", entity.getName(), e);
        }
        return PipelineService.super.deleteById(repository, id);
    }

    /**
     * Merges the properties of a source entity with a list of fields and a configuration.
     *
     * @param entity the source entity
     * @param fields the list of fields
     * @param configure the configuration
     * @return the merged properties
     */
    private Properties merge(SourceEntity entity, List<IConfigureExecutorField> fields, Properties configure)
    {
        Properties properties = new Properties();
        Properties convertBeanProperties = BeanToPropertiesUtils.convertBeanToProperties(entity);
        for (IConfigureExecutorField field : fields) {
            if (field.isOverride()) {
                this.setProperty(field, properties, configure);
            }
            else {
                this.setProperty(field, properties, convertBeanProperties);
            }
        }
        return properties;
    }

    /**
     * Sets the property value for the given field.
     *
     * @param field the field to set the property value for
     * @param properties the properties object to store the property
     * @param configure the configuration properties object
     */
    private void setProperty(IConfigureExecutorField field, Properties properties, Properties configure)
    {
        Object value = "None";
        if (ObjectUtils.isNotEmpty(field.getOrigin())) {
            String[] split = String.valueOf(field.getOrigin()).split("\\|");
            if (split.length > 1) {
                value = String.join(":", String.valueOf(configure.get(split[0])), String.valueOf(configure.get(split[1])));
            }
            else {
                if (ObjectUtils.isNotEmpty(configure.get(field.getOrigin()))) {
                    value = configure.get(field.getOrigin());
                }
            }
        }
        else {
            if (ObjectUtils.isNotEmpty(configure.get(field.getField()))) {
                value = configure.get(field.getField());
            }
        }
        properties.put(field.getField(), value);
    }

    private List<String> checkField(List<IConfigureExecutorField> fields, Properties configures, String name, String type)
    {
        List<String> list = Lists.newArrayList();
        fields.stream()
                .filter(field -> field.isInput())
                .forEach(field -> {
                    if (field.isRequired() && field.isInput() && field.isOverride()) {
                        if (ObjectUtils.isEmpty(configures.get(field.getField()))) {
                            list.add(String.format("The pipeline type [ %s ] of the [ %s ] field [ %s ] is a required field, please be sure to enter", type, name, field.getField()));
                        }
                    }
                    if (field.getType().equals(FieldType.SELECT)) {
                        if (!field.getDefaultValues().contains(configures.get(field.getField()))) {
                            list.add(String.format("The pipeline type [ %s ] of the [ %s ] field [ %s ] support the default value %s", type, name, field.getField(), String.join(",", field.getDefaultValues())));
                        }
                    }
                });
        return list;
    }
}
