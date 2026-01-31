package com.example.springbootnewsportal.service;

import com.example.springbootnewsportal.dto.request.auth.LoginRequestDto;
import com.example.springbootnewsportal.dto.request.user.RegisterRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import com.example.springbootnewsportal.dto.response.auth.AuthResponseDto;
import com.example.springbootnewsportal.exception.InvalidTokenException;
import com.example.springbootnewsportal.exception.ResourceAlreadyExistsException;
import com.example.springbootnewsportal.mapper.UserMapper;
import com.example.springbootnewsportal.model.Role;
import com.example.springbootnewsportal.model.User;
import com.example.springbootnewsportal.security.jwt.accesstoken.AccessTokenService;
import com.example.springbootnewsportal.security.user.CustomUserDetails;
import com.example.springbootnewsportal.service.api.UserService;
import com.example.springbootnewsportal.security.jwt.refreshtoken.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    // 1. Работа с access token
    private final AccessTokenService accessTokenService;

    // 2. Работа с refresh token
    private final RefreshTokenService refreshTokenService;

    // 3. Работа с пользователем
    private final UserService userService;

    // 4. Аутентификация логин/пароль
    private final AuthenticationManager authenticationManager;

    // 5. Маппинг User -> UserDto
    private final UserMapper userMapper;

    private final Duration refreshTokenTtl;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void register(RegisterRequestDto dto) {

        if (userService.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("User with this email already exists");
        }

        if (userService.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("User with this username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userService.save(user);
    }


    @Override
    public AuthResponseDto login(LoginRequestDto dto, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


        String accessToken = accessTokenService.generateAccessToken(userDetails);
        String refreshToken = refreshTokenService.generateRefreshToken(userDetails.getUserId());

        Cookie cookie = new Cookie("refreshToken", refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) refreshTokenTtl.getSeconds());

        response.addCookie(cookie);
        //вынести в отдельный метод

        User user = userService.findByIdOrThrow(userDetails.getUserId());
        UserResponseDto userInfo = userMapper.toResponseDto(user);

        return new AuthResponseDto(accessToken, userInfo);
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken, HttpServletResponse response) {

        if (!refreshTokenService.isValid(refreshToken)) {
            throw new InvalidTokenException("Refreshtoken is not valid");
        }


        Long userId = refreshTokenService.extractUserId(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Cannot extract user id"));

        User user = userService.findByIdOrThrow(userId);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        refreshTokenService.markAsUsed(refreshToken);

        String newAccessToken = accessTokenService.generateAccessToken(userDetails);

        String newRefreshToken = refreshTokenService.generateRefreshToken(user.getId());

        Cookie cookie = new Cookie("refreshToken", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) refreshTokenTtl.getSeconds());
        response.addCookie(cookie);

        UserResponseDto userInfo = userMapper.toResponseDto(user);

        return new AuthResponseDto(newAccessToken, userInfo);
    }

    @Override
    public void logout(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidTokenException("No refresh token provided");
        }
        // чистить куки, отправить null cookies
        refreshTokenService.revokeToken(refreshToken);
        refreshTokenService.markAsUsed(refreshToken);
    }
}

