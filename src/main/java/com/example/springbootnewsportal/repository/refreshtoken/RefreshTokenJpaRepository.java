package com.example.springbootnewsportal.repository.refreshtoken;

import com.example.springbootnewsportal.model.refreshtoken.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    Optional<RefreshTokenJpaEntity> findByToken(String token);

    void deleteByUserId(Long userId);

    Optional<Long> findUserByToken(String token);

    void deleteByToken(String token);
}
