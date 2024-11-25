package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.RoleEntity;
import io.edurt.datacap.service.record.TreeRecord;

import java.util.List;

public interface RoleService
        extends BaseService<RoleEntity>
{
    CommonResponse<List<TreeRecord>> getMenusByRoleId(Long roleId);
}
