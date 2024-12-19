package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.WorkflowEntity;

import java.util.List;

public interface WorkflowService
        extends BaseService<WorkflowEntity>
{
    CommonResponse<Boolean> stop(String code);

    CommonResponse<List<String>> log(String code);

    CommonResponse<String> restart(String code);
}
