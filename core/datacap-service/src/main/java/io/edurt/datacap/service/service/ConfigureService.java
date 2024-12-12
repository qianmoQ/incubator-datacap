package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.itransient.configuration.Configuration;

public interface ConfigureService
{
    CommonResponse<Configuration> getExecutor();
}
