package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.PipelineBody;
import io.edurt.datacap.service.entity.PipelineEntity;

import java.util.List;

public interface PipelineService
        extends BaseService<PipelineEntity>
{
    CommonResponse<Object> submit(PipelineBody configure);

    CommonResponse<Boolean> stop(Long id);

    CommonResponse<List<String>> log(String code);
}
