package com.example.springbootnewsportal.controller.auth;

import com.example.springbootnewsportal.dto.request.auth.LoginRequestDto;


import com.example.springbootnewsportal.dto.request.user.RegisterRequestDto;
import com.example.springbootnewsportal.dto.response.auth.AuthResponseDto;
import com.example.springbootnewsportal.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

// передавать сущность в качестве ответа без responseentity!

    //  Регистрация
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDto dto) {
        authService.register(dto);

        return ResponseEntity.ok().build();
    }

    //  Логин
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody @Valid LoginRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(dto, response));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        AuthResponseDto dto = authService.refreshToken(refreshToken, response);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refreshToken") String refreshToken) {
        authService.logout(refreshToken);

        // Очистим cookie на клиенте  лучше вынести в сервис
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }





}
