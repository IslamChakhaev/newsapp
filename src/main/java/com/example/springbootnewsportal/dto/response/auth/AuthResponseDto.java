package com.example.springbootnewsportal.dto.response.auth;

import com.example.springbootnewsportal.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;        // JWT
    private UserResponseDto user;      // Дополнительно, если хочешь вернуть пользователя
}
