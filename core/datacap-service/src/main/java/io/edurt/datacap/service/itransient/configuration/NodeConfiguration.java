package io.edurt.datacap.service.itransient.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeConfiguration
{
    private String id;
    private String label;
    private String category;
    private PositionConfiguration position;
    private List<NodeItemConfiguration> configure;
    private String description;
    private List<PortConfiguration> ports;
}
