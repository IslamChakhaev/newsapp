package com.example.springbootnewsportal.aop.universal;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorize {

    String[] roles() default {};

    boolean checkOwnership() default false;

    EntityType entity() default EntityType.NONE;

    String idParam() default "id";
}
