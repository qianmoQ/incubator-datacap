package io.edurt.datacap.service.source;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.schedule.ScheduledRunnable;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.service.SourceService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据源元数据同步定时任务运行器
 * Scheduled task runner for synchronizing data source metadata
 *
 * <p>
 * 该类使用函数式编程方式实现数据源元数据的并行同步
 * This class implements parallel synchronization of data source metadata using functional programming
 * </p>
 */
@Slf4j
@SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION", "EI_EXPOSE_REP2"})
public class SyncMetadataScheduledRunnable
        extends ScheduledRunnable
{
    /**
     * 插件管理器 Plugin manager
     */
    private final PluginManager pluginManager;

    /**
     * 数据源仓库 Source repository
     */
    private final SourceRepository sourceRepository;

    /**
     * 数据源服务 Source service
     */
    private final SourceService sourceService;

    /**
     * 构造函数 Constructor
     */
    public SyncMetadataScheduledRunnable(String name,
            PluginManager pluginManager,
            SourceRepository sourceRepository,
            SourceService sourceService)
    {
        super(name);
        this.pluginManager = pluginManager;
        this.sourceRepository = sourceRepository;
        this.sourceService = sourceService;
    }

    /**
     * 执行元数据同步任务
     * Execute metadata synchronization task
     */
    @Override
    public void run()
    {
        logTaskStart()
                .andThen(this::executeSyncTask)
                .andThen(this::logTaskEnd)
                .accept(getName());
    }

    /**
     * 记录任务开始
     * Log task start
     *
     * @return 返回一个消费者函数 Returns a consumer function
     */
    private Consumer<String> logTaskStart()
    {
        return taskName -> log.info("==================== {} started =================", taskName);
    }

    /**
     * 记录任务结束
     * Log task end
     */
    private void logTaskEnd(String taskName)
    {
        log.info("==================== {} ended =================", taskName);
    }

    /**
     * 执行同步任务
     * Execute synchronization task
     *
     * @param taskName 任务名称 Task name
     */
    private void executeSyncTask(String taskName)
    {
        try {
            sourceRepository.findAll()
                    .stream()
                    .map(this::createSyncOperation)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            this::executeParallelSync
                    ));
        }
        catch (Exception e) {
            log.error("Failed to execute metadata sync task: {}", e.getMessage(), e);
        }
    }

    /**
     * 创建同步操作
     * Create synchronization operation
     *
     * @param source 数据源 Source
     * @return 返回一个异步操作 Returns an async operation
     */
    private CompletableFuture<Void> createSyncOperation(SourceEntity source)
    {
        return CompletableFuture.runAsync(() ->
                Optional.of(source)
                        .map(SourceEntity::getCode)
                        .ifPresent(this::syncSourceMetadata)
        );
    }

    /**
     * 执行并行同步
     * Execute parallel synchronization
     *
     * @param futures 异步操作列表 List of async operations
     */
    private Void executeParallelSync(List<CompletableFuture<Void>> futures)
    {
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .join();
        }
        catch (Exception e) {
            log.error("Error in parallel execution: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 同步单个数据源的元数据
     * Synchronize metadata for a single data source
     *
     * @param code 数据源ID Source ID
     */
    private void syncSourceMetadata(String code)
    {
        Function<String, String> getSourceName = id ->
                sourceRepository.findByCode(id)
                        .map(SourceEntity::getName)
                        .orElse("Unknown");

        Consumer<String> syncOperation = id -> {
            String sourceName = getSourceName.apply(id);
            try {
                log.debug("Starting metadata sync for source: {}", sourceName);
                sourceService.syncMetadata(id);
                log.debug("Completed metadata sync for source: {}", sourceName);
            }
            catch (Exception e) {
                log.error("Failed to sync metadata for source {}: {}", sourceName, e.getMessage(), e);
            }
        };

        syncOperation.accept(code);
    }
}
