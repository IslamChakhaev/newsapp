package com.example.springbootnewsportal.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDto {
    @NotBlank(message = "Refresh token must not be blank")
    private String refreshToken;
}
