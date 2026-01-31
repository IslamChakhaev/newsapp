package com.example.springbootnewsportal.service;

import com.example.springbootnewsportal.dto.request.auth.LoginRequestDto;
import com.example.springbootnewsportal.dto.request.user.RegisterRequestDto;
import com.example.springbootnewsportal.dto.response.auth.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    //  Регистрация пользователя и возврат токенов
    void register(RegisterRequestDto dto);

    //  Вход по логину и паролю, возврат access/refresh токенов
    AuthResponseDto login(LoginRequestDto dto, HttpServletResponse response);

    //  Обновление access/refresh токенов по refreshToken
    AuthResponseDto refreshToken(String refreshToken, HttpServletResponse response);

    //  Logout: инвалидировать refresh token
    void logout(String refreshToken);


}
