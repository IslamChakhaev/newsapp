package com.example.springbootnewsportal.security.jwt.refreshtoken;


import java.util.Optional;

public interface RefreshTokenService {

    /**
     * Генерирует и сохраняет новый refresh-токен (в SQL и Redis).
     */
    String generateRefreshToken(Long userId);

    /**
     * Проверяет, действителен ли refresh-токен:
     * - существует,
     * - не истёк,
     * - не отозван,
     * - не использован ранее (если используется механизм single-use).
     */
    boolean isValid(String token);

    /**
     * Проверяет, истёк ли токен по TTL.
     */
    boolean isExpired(String token);

    /**
     * Извлекает userId, привязанный к refresh-токену (если он существует).
     */
    Optional<Long> extractUserId(String token);

    /**
     * Помечает refresh-токен как отозванный.
     */
    void revokeToken(String token);

    /**
     * Удаляет все refresh-токены, связанные с пользователем (по userId).
     */
    void deleteByUserId(Long userId);

    void markAsUsed(String token);

}


