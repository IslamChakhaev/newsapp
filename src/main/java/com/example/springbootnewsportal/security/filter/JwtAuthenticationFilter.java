package com.example.springbootnewsportal.security.filter;

import com.example.springbootnewsportal.security.jwt.accesstoken.AccessTokenService;
import com.example.springbootnewsportal.security.jwt.JwtTokenProperties;
import com.example.springbootnewsportal.security.scopes.AuthenticatedUser;
import com.example.springbootnewsportal.security.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenService accessTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProperties properties;
    private final AuthenticatedUser authenticatedUser;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(properties.getHeader());
        String expectedPrefix = properties.getTokenPrefix() + " ";

        try {
            if (authHeader != null && authHeader.startsWith(expectedPrefix)) {

                String token = authHeader.substring(expectedPrefix.length());

                if (accessTokenService.validate(token) && SecurityContextHolder.getContext().getAuthentication() == null) {

                    Long userId = accessTokenService.extractUserId(token);
                    UserDetails userDetails = customUserDetailsService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    authenticatedUser.setUserId(userId);
                    authenticatedUser.setUsername(userDetails.getUsername());
                    authenticatedUser.setRoles(
                            userDetails.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toSet())
                    );
                }
            }

        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }



}

