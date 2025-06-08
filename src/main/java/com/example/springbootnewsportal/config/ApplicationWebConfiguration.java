package com.example.springbootnewsportal.config;

import com.example.springbootnewsportal.interceptors.RequestTraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingControllerInterceptor());
    }

    @Bean
    public RequestTraceInterceptor loggingControllerInterceptor() {
        return new RequestTraceInterceptor();
    }
}
