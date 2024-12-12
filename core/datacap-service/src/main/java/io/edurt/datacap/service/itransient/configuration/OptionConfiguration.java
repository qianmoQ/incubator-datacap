package io.edurt.datacap.service.itransient.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OptionConfiguration
{
    private String label;
    private String value;
}
