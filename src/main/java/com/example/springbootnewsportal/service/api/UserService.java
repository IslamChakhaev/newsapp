package com.example.springbootnewsportal.service.api;


import com.example.springbootnewsportal.dto.request.user.ChangePasswordRequestDto;
import com.example.springbootnewsportal.dto.request.user.UpdateUserRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import com.example.springbootnewsportal.model.User;
import org.springframework.data.domain.Page;

public interface UserService {


        public User save(User user);

        UserResponseDto findById(Long id);

        Page<UserResponseDto> findAll(int page, int size);

        UserResponseDto update(Long id, UpdateUserRequestDto dto);

        void changePassword(Long userId, ChangePasswordRequestDto dto);

        void delete(Long id);

        User findByIdOrThrow(Long id); // для внутреннего использования


        boolean existsByEmail(String email);
        boolean existsByUsername(String username);
}
