package com.example.springbootnewsportal.aop.universal;

import com.example.springbootnewsportal.security.scopes.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final AuthenticatedUser authenticatedUser;
    private final OwnershipService ownershipService;

    @Before("@annotation(authorize)")
    public void authorize(JoinPoint joinPoint, Authorize authorize) {

        Long currentUserId = authenticatedUser.getUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        // 1. Проверка ролей
        for (String role : authorize.roles()) {
            if (authenticatedUser.hasRole(role)) {
                return;
            }
        }

        // 2. Проверка владения
        if (authorize.checkOwnership()) {
            Long entityId = extractId(joinPoint, authorize.idParam());

            if (ownershipService.isOwner(
                    authorize.entity(),
                    entityId,
                    currentUserId
            )) {
                return;
            }
        }

        throw new AccessDeniedException("Access denied");
    }

    private Long extractId(JoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(paramName)) {
                return (Long) args[i];
            }
        }

        throw new IllegalStateException(
                "Parameter '" + paramName + "' not found in method"
        );
    }
}
