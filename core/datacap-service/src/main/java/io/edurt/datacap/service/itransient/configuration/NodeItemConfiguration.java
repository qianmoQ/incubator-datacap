package io.edurt.datacap.service.itransient.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeItemConfiguration
{
    private String field;
    private String label;
    private String type;
    private String description;
    private boolean required;
    private String placeholder;
    private Object value;
    private List<RuleConfiguration> rules;
    private List<OptionConfiguration> options;
}
