package com.example.springbootnewsportal.service.impl;

import com.example.springbootnewsportal.aop.universal.Authorize;
import com.example.springbootnewsportal.aop.universal.EntityType;
import com.example.springbootnewsportal.dto.request.user.ChangePasswordRequestDto;
import com.example.springbootnewsportal.dto.request.user.RegisterRequestDto;
import com.example.springbootnewsportal.dto.request.user.UpdateUserRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import com.example.springbootnewsportal.exception.ResourceNotFoundException;
import com.example.springbootnewsportal.mapper.UserMapper;
import com.example.springbootnewsportal.mapper.UserPatcher;
import com.example.springbootnewsportal.model.User;
import com.example.springbootnewsportal.repository.UserRepository;
import com.example.springbootnewsportal.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserPatcher userPatcher;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Authorize(
            roles = {"ROLE_ADMIN"}
    )
    public Page<UserResponseDto> findAll(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size));
        return usersPage.map(userMapper::toResponseDto);
    }

    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.USER,
            idParam = "id"
    )
    public UserResponseDto findById(Long id) {
        User user = findByIdOrThrow(id);

        return userMapper.toResponseDto(user);
    }


    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.USER,
            idParam = "id"
    )
    @Transactional
    public void delete(Long id) {
        findByIdOrThrow(id);
        userRepository.deleteById(id);
    }


    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.USER,
            idParam = "id"
    )
    @Transactional
    public UserResponseDto update(Long id, UpdateUserRequestDto dto) {
        User user = findByIdOrThrow(id);

        user = userPatcher.patch(user, dto);

        User updated = userRepository.save(user);

        return userMapper.toResponseDto(updated);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequestDto dto) {
        User user = findByIdOrThrow(userId);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }


}
