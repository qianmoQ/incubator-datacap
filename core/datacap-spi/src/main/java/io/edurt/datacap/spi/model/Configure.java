package io.edurt.datacap.spi.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.plugin.Plugin;
import io.edurt.datacap.plugin.PluginManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", "CT_CONSTRUCTOR_THROW",
        "NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR"})
public class Configure
{
    @NonNull
    private Plugin plugin;
    private PluginManager pluginManager;
    private String host;
    private Integer port;
    private Optional<String> username = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> database = Optional.empty();
    private Optional<String> version = Optional.empty();
    private Optional<Map<String, Object>> env = Optional.empty();
    private Optional<Boolean> ssl = Optional.empty();
    private String format = "JsonConvert";
    // if `to`: skip
    private Optional<String> query = Optional.empty();
    // Support for custom upload configuration plugins
    private String home;
    private boolean usedConfig;
    private String id;
}
