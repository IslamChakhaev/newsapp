package com.example.springbootnewsportal.service;


import com.example.springbootnewsportal.dto.request.UserRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponseDto create(UserRequestDto dto);

    Page<UserResponseDto> findAll(int page, int size);

    UserResponseDto findById(Long id);

    void delete(Long id);


}
