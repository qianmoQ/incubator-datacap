package io.edurt.datacap.service.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TableFilter
{
    private int pageSize = 500;
    private int currentPage = 1;
    private List<OrderFilter> orders;
}
