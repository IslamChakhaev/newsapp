package com.example.springbootnewsportal.model.refreshtoken;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.time.Instant;

@RedisHash(value = "refreshToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRedisEntity implements Serializable {

    @TimeToLive
    private Long ttl;

    @Id
    private String token; // Ключ в Redis

    private Long userId;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean used;
    private boolean revoked;
}
