package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.plugin.PluginMetadata;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.adhoc.Adhoc;
import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.DatasetHistoryEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.spi.model.Response;

import java.util.List;
import java.util.Set;

public interface DataSetService
        extends BaseService<DataSetEntity>
{
    CommonResponse<DataSetEntity> rebuild(String code);

    CommonResponse<List<DataSetColumnEntity>> getColumnsByCode(String code);

    CommonResponse<Boolean> syncData(String code);

    CommonResponse<Boolean> clearData(String code);

    CommonResponse<Response> adhoc(String code, Adhoc configure);

    CommonResponse<Set<PluginMetadata>> getActuators();

    CommonResponse<DataSetEntity> getInfo(String code);

    CommonResponse<PageEntity<DatasetHistoryEntity>> getHistory(String code, FilterBody filter);
}
