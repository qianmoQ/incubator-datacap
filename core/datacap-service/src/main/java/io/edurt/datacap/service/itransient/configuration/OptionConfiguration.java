package io.edurt.datacap.service.itransient.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.edurt.datacap.common.view.EntityView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionConfiguration
{
    private String label;
    private String value;
}
