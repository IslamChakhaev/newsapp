package com.example.springbootnewsportal.security.scopes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

@Component
@RequestScope
@Getter @Setter
public class AuthenticatedUser {
    private Long userId;
    private String username;
    private Set<String> roles;

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
}

