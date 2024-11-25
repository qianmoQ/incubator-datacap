package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.UserEntity;

import java.util.Optional;

public interface UserRepository
        extends BaseRepository<UserEntity, Long>
{
    Optional<UserEntity> findByUsername(String username);

    UserEntity findByUsernameAndSystemTrue(String username);
}
