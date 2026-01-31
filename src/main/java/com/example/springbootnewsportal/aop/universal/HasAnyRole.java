package com.example.springbootnewsportal.aop.universal;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasAnyRole {
    String[] value(); // список ролей, например {"ROLE_ADMIN", "ROLE_MODERATOR"}
}