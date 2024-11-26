package io.edurt.datacap.service.service;

import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.CodeUtils;
import io.edurt.datacap.common.utils.NullAwareBeanUtils;
import io.edurt.datacap.common.utils.ReflectionUtils;
import io.edurt.datacap.service.SelfException;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.BaseEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

public interface BaseService<T extends BaseEntity>
{
    default CommonResponse<PageEntity<T>> getAll(BaseRepository<T, Long> repository, FilterBody filter)
    {
        try {
            Pageable pageable = PageRequestAdapter.of(filter);

            try {
                return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
            }
            catch (UnsupportedOperationException e) {
                return CommonResponse.success(PageEntity.build(repository.findAll(pageable)));
            }
        }
        catch (Exception e) {
            return CommonResponse.failure("Failed to fetch data: " + e.getMessage());
        }
    }

    default CommonResponse<T> getById(BaseRepository<T, Long> repository, Long id)
    {
        return repository.findById(id)
                .map(CommonResponse::success)
                .orElse(CommonResponse.failure(String.format("Resource [ %s ] not found", id)));
    }

    default CommonResponse<T> saveOrUpdate(BaseRepository<T, Long> repository, T configure)
    {
        if (configure.getId() != null) {
            repository.findById(configure.getId())
                    .ifPresent(value -> NullAwareBeanUtils.copyNullProperties(value, configure));
        }
        if (ReflectionUtils.hasField(configure, "user")) {
            ReflectionUtils.setFieldValue(configure, "user", UserDetailsService.getUser());
        }
        if (StringUtils.isEmpty(configure.getCode())) {
            configure.setCode(CodeUtils.generateCode(false));
        }
        return CommonResponse.success(repository.save(configure));
    }

    default CommonResponse<Long> deleteById(BaseRepository<T, Long> repository, Long id)
    {
        repository.deleteById(id);
        return CommonResponse.success(id);
    }

    default CommonResponse<String> deleteByCode(BaseRepository<T, Long> repository, String code)
    {
        return repository.findByCode(code)
                .map(entity -> {
                    repository.delete(entity);
                    return CommonResponse.success(code);
                })
                .orElseGet(() -> CommonResponse.failure(String.format("Resource [ %s ] not found", code)));
    }

    default CommonResponse<T> getByCode(BaseRepository<T, Long> repository, String code)
    {
        return repository.findByCode(code)
                .map(this::validatorUser)
                .orElseGet(() -> CommonResponse.failure(String.format("Resource [ %s ] not found", code)));
    }

    default <R> CommonResponse<R> validatorUser(R value)
    {
        if (ReflectionUtils.hasField(value, "user")) {
            UserEntity originalUser = (UserEntity) ReflectionUtils.getFieldValue(value, "user");
            UserEntity loginUser = UserDetailsService.getUser();
            if (originalUser != null && !originalUser.getId().equals(loginUser.getId())) {
                return CommonResponse.failure(ServiceState.USER_UNAUTHORIZED);
            }
        }
        return CommonResponse.success(value);
    }

    default void isSelf(Object value)
    {
        if (ReflectionUtils.hasField(value, "user")) {
            UserEntity originalUser = (UserEntity) ReflectionUtils.getFieldValue(value, "user");
            UserEntity loginUser = UserDetailsService.getUser();
            if (originalUser != null && !originalUser.getId().equals(loginUser.getId())) {
                throw new SelfException(String.format("Resource [ %s ] not found", value.getClass().getName()));
            }
        }
    }
}
