package com.example.springbootnewsportal.filter;

import com.example.springbootnewsportal.scopes.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${app.check-user-id-header:true}")
public class UserIdHeaderFilter extends OncePerRequestFilter {

    private final AuthenticatedUser authenticatedUser;

    private static final String HEADER_NAME = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userIdHeader = request.getHeader(HEADER_NAME);

        if (userIdHeader == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Заголовок X-User-Id обязателен");
            return;
        }

        try {
            Long userId = Long.parseLong(userIdHeader);
            authenticatedUser.setUserId(userId);
        } catch (NumberFormatException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "X-User-Id должен быть числом");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
