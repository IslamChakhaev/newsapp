package com.example.springbootnewsportal.repository.refreshtoken;

import com.example.springbootnewsportal.model.refreshtoken.RefreshTokenRedisEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenRedisEntity, String> {

    Optional<RefreshTokenRedisEntity> findByToken(String token);

    void deleteByToken(String token);


    void deleteByUserId(Long userId);
}

