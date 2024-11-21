package io.edurt.datacap.server.controller;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.executor.ExecutorService;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.plugin.PluginMetadata;
import io.edurt.datacap.plugin.PluginType;
import io.edurt.datacap.scheduler.SchedulerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/plugin")
public class PluginController
{
    private final PluginManager pluginManager;
    private final Injector injector;

    public PluginController(PluginManager pluginManager, Injector injector)
    {
        this.pluginManager = pluginManager;
        this.injector = injector;
    }

    @GetMapping
    public CommonResponse<Map<String, Set<String>>> getPlugins()
    {
        Map<String, Set<String>> plugins = Maps.newHashMap();
        Set<String> executors = injector.getInstance(Key.get(new TypeLiteral<Set<ExecutorService>>() {}))
                .stream()
                .map(ExecutorService::name)
                .collect(Collectors.toSet());
        plugins.put("executor", executors);

        Set<String> schedulers = injector.getInstance(Key.get(new TypeLiteral<Set<SchedulerService>>() {}))
                .stream()
                .map(SchedulerService::name)
                .collect(Collectors.toSet());
        plugins.put("scheduler", schedulers);
        return CommonResponse.success(plugins);
    }

    @GetMapping(value = {"filter"})
    public CommonResponse getPluginByType(@RequestParam String type)
    {
        if (type.equalsIgnoreCase("plugin")) {
            List<PluginMetadata> plugins = pluginManager.getPluginInfos().stream()
                    .filter(v -> v.getType().equals(PluginType.CONNECTOR))
                    .collect(Collectors.toList());
            return CommonResponse.success(plugins);
        }
        else {
            return CommonResponse.failure("Unknown type " + type);
        }
    }
}
