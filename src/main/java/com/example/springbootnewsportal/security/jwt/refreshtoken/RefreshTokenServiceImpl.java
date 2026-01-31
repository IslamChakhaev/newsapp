    package com.example.springbootnewsportal.security.jwt.refreshtoken;

    import com.example.springbootnewsportal.model.refreshtoken.RefreshTokenJpaEntity;
    import com.example.springbootnewsportal.model.refreshtoken.RefreshTokenRedisEntity;
    import com.example.springbootnewsportal.repository.refreshtoken.RefreshTokenJpaRepository;
    import com.example.springbootnewsportal.repository.refreshtoken.RefreshTokenRedisRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.Clock;
    import java.time.Duration;
    import java.time.Instant;
    import java.util.Optional;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class RefreshTokenServiceImpl implements RefreshTokenService {

        private final Clock clock;
        private final Duration refreshTokenTtl;
        private final RefreshTokenJpaRepository jpaRepository;
        private final RefreshTokenRedisRepository redisRepository;


        @Override
        @Transactional
        public String generateRefreshToken(Long userId) {

            String token = UUID.randomUUID().toString();
            Instant iat = clock.instant();
            Instant exp = iat.plus(refreshTokenTtl);


            RefreshTokenJpaEntity entitySql = RefreshTokenJpaEntity.builder()
                    .token(token)
                    .userId(userId)
                    .createdAt(iat)
                    .expiresAt(exp)
                    .used(false)
                    .revoked(false)
                    .build();

            RefreshTokenRedisEntity entityRedis = RefreshTokenRedisEntity.builder()
                    .ttl(refreshTokenTtl.getSeconds())
                    .token(token)
                    .userId(userId)
                    .createdAt(iat)
                    .expiresAt(exp)
                    .used(false)
                    .revoked(false)
                    .build();

            jpaRepository.save(entitySql);
            redisRepository.save(entityRedis);

            return token;
        }

        @Override
        public boolean isValid(String token) {
            var redisEntity = findInRedis(token);
            if (redisEntity.isPresent()) {
                RefreshTokenRedisEntity entity = redisEntity.get();
                return !entity.isRevoked() && !entity.isUsed() && !isExpired(token);
            }

            var jpaEntity = findInSql(token);
            if (jpaEntity.isPresent()) {
                RefreshTokenJpaEntity entity = jpaEntity.get();
                RefreshTokenRedisEntity restored = convertToRedis(entity);
                redisRepository.save(restored);
                return !entity.isRevoked() && !entity.isUsed() && !isExpired(token);
            }

            return false;
        }

        @Override
        public Optional<Long> extractUserId(String token) {
            Optional<RefreshTokenRedisEntity> opt = findInRedis(token);
            if (opt.isPresent()) {
                return opt.map(RefreshTokenRedisEntity::getUserId);
            }

            Optional<RefreshTokenJpaEntity> opt1 = findInSql(token);
            if (opt1.isPresent()) {
                RefreshTokenRedisEntity restored = convertToRedis(opt1.get());
                redisRepository.save(restored);
                return Optional.of(opt1.get().getUserId());
            }

            return Optional.empty();
        }

        @Override
        public boolean isExpired(String token) {
            var redisEntity = findInRedis(token);
            if (redisEntity.isPresent()) {
                return redisEntity.get().getExpiresAt().isBefore(clock.instant());
            }

            var jpaEntity = findInSql(token);
            if (jpaEntity.isPresent()) {
                RefreshTokenRedisEntity restored = convertToRedis(jpaEntity.get());
                redisRepository.save(restored);
                return jpaEntity.get().getExpiresAt().isBefore(clock.instant());
            }

            return true;
        }

        @Override
        public void revokeToken(String token) {
            var redisEntity = findInRedis(token);
            if (redisEntity.isPresent()) {
                redisEntity.get().setRevoked(true);
                redisRepository.save(redisEntity.get());
            }

            var jpaEntity = findInSql(token);
            if (jpaEntity.isPresent()) {
                jpaEntity.get().setRevoked(true);
                jpaRepository.save(jpaEntity.get());

                RefreshTokenRedisEntity restored = convertToRedis(jpaEntity.get());
                redisRepository.save(restored);
            }

            //  Удаляем из Redis (токен отработал, не нужен в быстром доступе)
            redisRepository.deleteByToken(token);

        }

        @Override
        public void deleteByUserId(Long userId) {
            redisRepository.deleteByUserId(userId);
            jpaRepository.deleteByUserId(userId);
        }

        @Override
        public void markAsUsed(String token) {
            // 1. Обновляем в Redis (если есть)
            findInRedis(token).ifPresent(redisEntity -> {
                redisEntity.setUsed(true);
                redisRepository.save(redisEntity);
            });

            // 2. Обновляем в SQL (важно — именно долговременное хранилище)
            findInSql(token).ifPresent(jpaEntity -> {
                jpaEntity.setUsed(true);
                jpaRepository.save(jpaEntity);
            });

            // 3. Удаляем из Redis (токен отработал, не нужен в быстром доступе)
            redisRepository.deleteByToken(token);
        }


        private RefreshTokenRedisEntity convertToRedis(RefreshTokenJpaEntity sqlEntity) {
            return RefreshTokenRedisEntity.builder()
                    .token(sqlEntity.getToken())
                    .userId(sqlEntity.getUserId())
                    .createdAt(sqlEntity.getCreatedAt())
                    .expiresAt(sqlEntity.getExpiresAt())
                    .used(sqlEntity.isUsed())
                    .revoked(sqlEntity.isRevoked())
                    .ttl(refreshTokenTtl.getSeconds())
                    .build();
        }

        public Optional<RefreshTokenRedisEntity> findInRedis(String token) {
            return redisRepository.findByToken(token);
        }

        public Optional<RefreshTokenJpaEntity> findInSql(String token) {
            return jpaRepository.findByToken(token);
        }
    }
