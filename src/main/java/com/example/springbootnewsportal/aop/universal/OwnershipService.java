package com.example.springbootnewsportal.aop.universal;

public interface OwnershipService {
    boolean isOwner(EntityType entity, Long entityId, Long userId);
}
