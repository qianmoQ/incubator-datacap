package io.edurt.datacap.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

@Data
@Builder
public class PluginMetadata
{
    private String name;
    private String version;
    private PluginState state;
    private long loadTimestamp;
    private String loadTime;
    private PluginType type;
    private String loaderName;

    @JsonIgnore
    private ClassLoader classLoader;

    @JsonIgnore
    private Object instance;

    @JsonIgnore
    private Path location;
}
