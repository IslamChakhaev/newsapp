package com.example.springbootnewsportal.aop.universal;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsOwner {

    String entity();   // Тип сущности, например "USER", "NEWS", "COMMENT"
    String idParam();  // Название параметра метода, в котором находится ID сущности
}
