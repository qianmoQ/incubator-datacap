package io.edurt.datacap.service.initializer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.scheduler.SchedulerRequest;
import io.edurt.datacap.scheduler.SchedulerService;
import io.edurt.datacap.service.enums.SyncMode;
import io.edurt.datacap.service.initializer.job.DatasetJob;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.service.DataSetService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class DatasetSchedulerInitializer
        implements CommandLineRunner
{
    private final PluginManager pluginManager;
    private final DataSetRepository repository;
    private final DataSetService service;
    private final Scheduler scheduler;

    public DatasetSchedulerInitializer(PluginManager pluginManager, DataSetRepository repository, DataSetService service, Scheduler scheduler)
    {
        this.pluginManager = pluginManager;
        this.repository = repository;
        this.service = service;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args)
            throws Exception
    {
        log.info("Start dataset scheduler initializer");
        this.repository.findAllBySyncMode(SyncMode.TIMING)
                .forEach(item -> {
                    log.info("Dataset [ {} ] will be scheduled", item.getName());
                    pluginManager.getPlugin(item.getScheduler())
                            .ifPresent(scheduler -> {
                                SchedulerRequest request = new SchedulerRequest();
                                request.setName(item.getCode());
                                request.setGroup("datacap");
                                request.setExpression(item.getExpression());
                                request.setJobId(item.getCode());
                                request.setCreateBeforeDelete(true);

                                SchedulerService schedulerService = scheduler.getService(SchedulerService.class);
                                if (schedulerService.name().equals("Default")) {
                                    request.setJob(new DatasetJob());
                                    request.setScheduler(this.scheduler);
                                }
                                schedulerService.initialize(request);
                            });
                });
        log.info("End dataset scheduler initializer");
    }
}
