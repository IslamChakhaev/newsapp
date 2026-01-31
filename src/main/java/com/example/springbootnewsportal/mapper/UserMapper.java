package com.example.springbootnewsportal.mapper;

import com.example.springbootnewsportal.dto.request.user.RegisterRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import com.example.springbootnewsportal.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "newsList", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", ignore = true) // шифруем вручную
    User toEntity(RegisterRequestDto dto);

    UserResponseDto toResponseDto(User user);



}

