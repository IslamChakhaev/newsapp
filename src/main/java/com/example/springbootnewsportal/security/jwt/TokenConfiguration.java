package com.example.springbootnewsportal.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;

@Configuration
public class TokenConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Duration accessTokenTtl(JwtTokenProperties jwtTokenProperties) {
        return jwtTokenProperties.getAccess().getExpiration();
    }

    @Bean
    public Duration refreshTokenTtl(JwtTokenProperties jwtTokenProperties) {
        return jwtTokenProperties.getRefresh().getExpiration();
    }

    @Bean
    public SecretKey accessTokenSecretKey(JwtTokenProperties jwtTokenProperties) {
        return Keys.hmacShaKeyFor(jwtTokenProperties.getAccess().getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public SecretKey refreshTokenSecretKey(JwtTokenProperties jwtTokenProperties) {
        return Keys.hmacShaKeyFor(jwtTokenProperties.getRefresh().getSecret().getBytes(StandardCharsets.UTF_8));
    }

}
