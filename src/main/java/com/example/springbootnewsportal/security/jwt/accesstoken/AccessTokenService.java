package com.example.springbootnewsportal.security.jwt.accesstoken;

import com.example.springbootnewsportal.security.jwt.JwtTokenProperties;
import com.example.springbootnewsportal.security.user.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessTokenService {

    private final JwtTokenProperties properties;
    private final Clock clock;
    private final Duration accessTokenTtl;
    private final SecretKey accessTokenSecretKey;


    public String generateAccessToken(CustomUserDetails customUserDetails) {

        Instant iat = clock.instant();
        Instant exp = iat.plus(accessTokenTtl);

        Claims claims = Jwts.claims();
        claims.setSubject(customUserDetails.getUserId().toString());
        claims.setIssuedAt(Date.from(iat));
        claims.setExpiration(Date.from(exp));

        claims.put("roles", customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(accessTokenSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessTokenSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public Set<String> extractRoles(String token) {
        Claims claims = parseClaims(token);
        return claims.get("roles", Set.class);
    }

    public boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        Instant exp = claims.getExpiration().toInstant();

        return exp.isBefore(clock.instant());
    }

    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);
        Long id = Long.parseLong(claims.getSubject());

        return id;
    }



    public Claims parseClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(accessTokenSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}