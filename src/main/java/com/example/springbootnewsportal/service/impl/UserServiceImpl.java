package com.example.springbootnewsportal.service.impl;

import com.example.springbootnewsportal.dto.request.UserRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import com.example.springbootnewsportal.entity.User;
import com.example.springbootnewsportal.exception.ResourceNotFoundException;
import com.example.springbootnewsportal.mapper.UserMapper;
import com.example.springbootnewsportal.repository.UserRepository;
import com.example.springbootnewsportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);

        return userMapper.toResponseDto(saved);
    }

    @Override
    public Page<UserResponseDto> findAll(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size));
        return usersPage.map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = findByIdOrThrow(id);

        return userMapper.toResponseDto(user);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        findByIdOrThrow(id);
        userRepository.deleteById(id);
    }


    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
