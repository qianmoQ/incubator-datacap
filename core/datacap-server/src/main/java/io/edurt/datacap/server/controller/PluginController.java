package io.edurt.datacap.server.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.plugin.PluginManager;
import io.edurt.datacap.plugin.PluginMetadata;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/plugin")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
public class PluginController
{
    private final PluginManager pluginManager;

    public PluginController(PluginManager pluginManager)
    {
        this.pluginManager = pluginManager;
    }

    @GetMapping
    public CommonResponse<List<PluginMetadata>> getPlugins()
    {
        return CommonResponse.success(pluginManager.getPluginInfos());
    }

    @PostMapping(value = "install")
    public CommonResponse<Boolean> installPlugin(@RequestBody PluginInstallRequest request)
    {
        return CommonResponse.success(pluginManager.installPlugin(Path.of(request.url), request.name));
    }

    @DeleteMapping(value = "uninstall/{name}")
    public CommonResponse<Boolean> uninstallPlugin(@PathVariable(value = "name") String name)
    {
        return CommonResponse.success(pluginManager.uninstallPlugin(name));
    }

    @Data
    public static class PluginInstallRequest
    {
        private String url;
        // 插件名称，默认作为插件的安装目录
        // Plugin name, default as the installation directory
        private String name;
    }
}
