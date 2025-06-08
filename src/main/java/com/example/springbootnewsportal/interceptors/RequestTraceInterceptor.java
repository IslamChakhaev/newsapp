package com.example.springbootnewsportal.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class RequestTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("Запрос [{} {}] принят в перехватчике {}", method, uri, this.getClass().getSimpleName());

        if (handler instanceof HandlerMethod handlerMethod) {
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String controllerMethod = handlerMethod.getMethod().getName();
            log.info("Запрос будет обработан контроллером: {}.{}()", controllerName, controllerMethod);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        int status = response.getStatus();
        log.info("Контроллер завершил обработку. HTTP статус: {}", status);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        if (ex != null) {
            log.error("Ошибка при обработке [{} {}]. HTTP статус: {}. Причина: {}",
                    method, uri, status, ex.getMessage(), ex);
        } else {
            log.info("Ответ на [{} {}] отправлен клиенту. HTTP статус: {}",
                    method, uri, status);
        }
    }
}
